/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.domain.dataschemas.upload;

/**
 *
 * @author cwfr-fdubois
 */
public enum UpdatePolicy {

    /** Erase existing values. */
    ERASE,
    /** Min of the existing value and the new one. */
    MIN,
    /** Max of the existing value and the new one. */
    MAX,
    /** Sum existing value with the new one. */
    SUM,
    /** Average of existing value and the new one. */
    AVG;

}
