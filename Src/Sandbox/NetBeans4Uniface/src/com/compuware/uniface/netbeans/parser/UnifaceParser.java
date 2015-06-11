/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.uniface.netbeans.parser;

import java.io.Reader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeListener;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.api.Task;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.SourceModificationEvent;

public class UnifaceParser extends Parser {

    private Snapshot snapshot;
    private com.compuware.uniface.netbeans.jccparser.UnifaceParser unifaceParser;

    @Override
    public void parse (Snapshot snapshot, Task task, SourceModificationEvent event) {
        this.snapshot = snapshot;
        Reader reader = new StringReader (snapshot.getText ().toString ());
        unifaceParser = new com.compuware.uniface.netbeans.jccparser.UnifaceParser (reader);
        try {
            unifaceParser.CompilationUnit();
        } catch (com.compuware.uniface.netbeans.jccparser.ParseException ex) {
            Logger.getLogger (UnifaceParser.class.getName()).log (Level.WARNING, null, ex);
        }
    }

    @Override
    public Result getResult (Task task) {
        return new UnifaceParserResult (snapshot, unifaceParser);
    }

    @Override
    public void cancel () {
    }

    @Override
    public void addChangeListener (ChangeListener changeListener) {
    }

    @Override
    public void removeChangeListener (ChangeListener changeListener) {
    }


    public static class UnifaceParserResult extends Result {

        private com.compuware.uniface.netbeans.jccparser.UnifaceParser unifaceParser;
        private boolean valid = true;

        UnifaceParserResult (Snapshot snapshot, com.compuware.uniface.netbeans.jccparser.UnifaceParser unifaceParser) {
            super (snapshot);
            this.unifaceParser = unifaceParser;
        }

        public com.compuware.uniface.netbeans.jccparser.UnifaceParser getUnifaceParser () throws org.netbeans.modules.parsing.spi.ParseException {
            if (!valid) throw new org.netbeans.modules.parsing.spi.ParseException ();
            return unifaceParser;
        }

        @Override
        protected void invalidate () {
            valid = false;
        }
    }
}
