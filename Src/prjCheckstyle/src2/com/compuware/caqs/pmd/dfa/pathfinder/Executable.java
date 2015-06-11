/*
 * Created on 09.08.2004
 */
package com.compuware.caqs.pmd.dfa.pathfinder;


/**
 * @author raik
 *         <p/>
 *         Will be executed if PathFinder finds a path.
 */
public interface Executable {

    void execute(CurrentPath path);
}
