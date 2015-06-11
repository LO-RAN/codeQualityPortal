/*
 * @author laurent IZAC laurent.izac@compuware.com
*/

package com.compuware.pmd.uniface.ast;

import net.sourceforge.pmd.ast.JavaCharStream;

import java.io.InputStream;
import java.io.Reader;

public class UnifaceCharStream extends JavaCharStream implements CharStream {

    public UnifaceCharStream(InputStream dstream, int startline, int startcolumn, int buffersize) {
        super(dstream, startline, startcolumn, buffersize);
        // TODO Auto-generated constructor stub
    }

    public UnifaceCharStream(InputStream dstream, int startline, int startcolumn) {
        super(dstream, startline, startcolumn);
        // TODO Auto-generated constructor stub
    }

    public UnifaceCharStream(InputStream dstream) {
        super(dstream);
        // TODO Auto-generated constructor stub
    }

    public UnifaceCharStream(Reader dstream, int startline, int startcolumn, int buffersize) {
        super(dstream, startline, startcolumn, buffersize);
        // TODO Auto-generated constructor stub
    }

    public UnifaceCharStream(Reader dstream, int startline, int startcolumn) {
        super(dstream, startline, startcolumn);
        // TODO Auto-generated constructor stub
    }

    public UnifaceCharStream(Reader dstream) {
        super(dstream);
        // TODO Auto-generated constructor stub
    }

}
