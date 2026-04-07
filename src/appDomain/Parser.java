package appDomain;

import implementations.MyQueue;
import implementations.MyStack;
import utilities.Iterator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Parser is the main application class that reads an XML file and validates
 * whether all tags are correctly opened and closed.
 * </p>
 *
 *
 */
public class Parser
{
    /**
     * <p>
     * Tag is a helper class that stores information about a parsed XML tag,
     * including its name, full text, line number, parse order, and type
     * (opening, closing, or self-closing).
     * </p>
     */
    static class Tag
    {
        // The tag name
        String name;
        // full tag name
        String fullText;
        // The line number in the XML file where this tag was found
        int line;
        // The order in which this tag was parsed, used for sorting errors
        int order;
        // True if this is an opening tag 
        boolean isStart;
        // True if this is a closing tag 
        boolean isEnd;
        // True if this is a self-closing tag 
        boolean isSelfClosing;

        /**
         * Constructs a Tag with all its properties.
         *
         * @param name         The name of the tag.
         * @param fullText     The full tag text including angle brackets.
         * @param line         The line number where the tag appears.
         * @param order        The parse order of the tag.
         * @param isStart      True if this is an opening tag.
         * @param isEnd        True if this is a closing tag.
         * @param isSelfClosing True if this is a self-closing tag.
         */
        public Tag( String name, String fullText, int line, int order,
                    boolean isStart, boolean isEnd, boolean isSelfClosing )
        {
            this.name = name;
            this.fullText = fullText;
            this.line = line;
            this.order = order;
            this.isStart = isStart;
            this.isEnd = isEnd;
            this.isSelfClosing = isSelfClosing;
        }
    }

    /**
     * The main method that drives the XML parsing process.
     * Reads the XML file, parses all tags, runs Kitty's Algorithm,
     * and prints any errors found.
     *
     * @param args Command-line arguments. args[0] should be the path to the XML file.
     */
    public static void main( String[] args )
    {
        // Ensure a file path was provided
        if( args.length == 0 )
        {
            System.out.println( "Usage: java -jar Parser.jar <filename.xml>" );
            return;
        }

        // List to hold all parsed tags in document order
        List<Tag> tags = new ArrayList<>();
        int orderCounter = 0;

        
        // Read the XML file and extract all tags using regex
        
        try( BufferedReader br = new BufferedReader( new FileReader( args[0] ) ) )
        {
            String line;
            int lineNumber = 1;

            // Regex pattern to match anything inside angle brackets
            Pattern p = Pattern.compile( "<([^>]+)>" );

            while( ( line = br.readLine() ) != null )
            {
                Matcher m = p.matcher( line );
                while( m.find() )
                {
                    String content = m.group( 1 ).trim();

                    // Skip XML declarations like <?xml version="1.0"?>
                    if( content.startsWith( "?" ) ) continue;

                    String fullTag = "<" + content + ">";

                    // Determine tag type
                    boolean isEnd = content.startsWith( "/" );
                    boolean isSelfClosing = content.endsWith( "/" );

                    // Strip leading slash for closing tags
                    String namePart = content;
                    if( isEnd ) namePart = namePart.substring( 1 ).trim();
                    // Strip trailing slash for self-closing tags
                    if( isSelfClosing ) namePart = namePart.substring( 0, namePart.length() - 1 ).trim();

                    // Extract tag name only, ignoring any attributes
                    String name = namePart.split( "\\s+" )[0];
                    boolean isStart = !isEnd && !isSelfClosing;

                    // Add the parsed tag to our list
                    tags.add( new Tag( name, fullTag, lineNumber, orderCounter++,
                                       isStart, isEnd, isSelfClosing ) );
                }
                lineNumber++;
            }
        }
        catch( Exception e )
        {
            System.out.println( "Error reading file: " + e.getMessage() );
            return;
        }

        // 
        // Run Kitty's Algorithm using MyStack and MyQueue
        // 

        // Stack holds opening tags waiting to be matched
        MyStack<Tag> stack = new MyStack<>();
        // errorQ holds unmatched opening tags
        MyQueue<Tag> errorQ = new MyQueue<>();
        // extrasQ holds closing tags with no matching opening tag
        MyQueue<Tag> extrasQ = new MyQueue<>();
        // Final list of all error tags to be printed
        List<Tag> errors = new ArrayList<>();

        for( Tag T : tags )
        {
            // Self-closing tags are always valid, skip them
            if( T.isSelfClosing ) continue;

            if( T.isStart )
            {
                // Push opening tags onto the stack
                stack.push( T );
            }
            else if( T.isEnd )
            {
                try
                {
                    if( !stack.isEmpty() && stack.peek().name.equals( T.name ) )
                    {
                        // Perfect match at top of stack — pop it
                        stack.pop();
                    }
                    else if( !errorQ.isEmpty() && errorQ.peek().name.equals( T.name ) )
                    {
                        // Matches the front of the error queue — dequeue it
                        errorQ.dequeue();
                    }
                    else if( stack.isEmpty() )
                    {
                        // No opening tag to match — this closing tag is extra
                        extrasQ.enqueue( T );
                    }
                    else
                    {
                        // Check deeper in the stack for a matching opening tag
                        boolean hasMatch = false;
                        Iterator<Tag> it = stack.iterator();
                        while( it.hasNext() )
                        {
                            if( it.next().name.equals( T.name ) )
                            {
                                hasMatch = true;
                                break;
                            }
                        }

                        if( hasMatch )
                        {
                            // Pop tags off the stack until we find the match
                            // Tags popped before the match are errors (not closed properly)
                            while( !stack.isEmpty() )
                            {
                                Tag top = stack.peek();
                                if( top.name.equals( T.name ) )
                                {
                                    // Found the match — pop and stop
                                    stack.pop();
                                    break;
                                }
                                else
                                {
                                    // This tag was never closed — it's an error
                                    Tag popped = stack.pop();
                                    errorQ.enqueue( popped );
                                    errors.add( popped );
                                }
                            }
                        }
                        else
                        {
                            // No match anywhere in the stack — extra closing tag
                            extrasQ.enqueue( T );
                        }
                    }
                }
                catch( Exception e )
                {
                    System.err.println( "Exception during parsing: " + e.getMessage() );
                }
            }
        }

        // ---------------------------------------------------------------
        // STEP 3: Cleanup — handle leftover tags in stack and queues
        // ---------------------------------------------------------------
        try
        {
            // Any tags left on the stack were never closed — they are errors
            while( !stack.isEmpty() )
            {
                Tag popped = stack.pop();
                errorQ.enqueue( popped );
            }

            if( errorQ.isEmpty() ^ extrasQ.isEmpty() )
            {
                // Only one queue has items — all of them are errors
                while( !errorQ.isEmpty() ) errors.add( errorQ.dequeue() );
                while( !extrasQ.isEmpty() ) errors.add( extrasQ.dequeue() );
            }
            else if( !errorQ.isEmpty() && !extrasQ.isEmpty() )
            {
                // Both queues have items — try to pair them up
                // Paired tags cancel each other out, unpaired ones are errors
                while( !errorQ.isEmpty() && !extrasQ.isEmpty() )
                {
                    Tag err = errorQ.peek();
                    Tag ext = extrasQ.peek();

                    if( !err.name.equals( ext.name ) )
                    {
                        // Names don't match — the error tag is a real error
                        errors.add( errorQ.dequeue() );
                    }
                    else
                    {
                        // Names match — they cancel each other out
                        errorQ.dequeue();
                        extrasQ.dequeue();
                    }
                }
                // Add any remaining unpaired tags as errors
                while( !errorQ.isEmpty() ) errors.add( errorQ.dequeue() );
                while( !extrasQ.isEmpty() ) errors.add( extrasQ.dequeue() );
            }
        }
        catch( Exception e )
        {
            System.err.println( "Cleanup exception: " + e.getMessage() );
        }

        // 
        // STEP 4: Print results sorted by line number then parse order
        // 
        Collections.sort( errors, new Comparator<Tag>()
        {
            @Override
            public int compare( Tag t1, Tag t2 )
            {
                // Sort by line number first
                if( t1.line != t2.line ) return Integer.compare( t1.line, t2.line );
                // Break ties using parse order
                return Integer.compare( t1.order, t2.order );
            }
        } );

        if( errors.isEmpty() )
        {
            System.out.println( "XML document is constructed correctly." );
        }
        else
        {
            for( Tag err : errors )
            {
                System.out.println( "Error at line: " + err.line + " " +
                                    err.fullText + " is not constructed correctly." );
            }
        }
    }
}