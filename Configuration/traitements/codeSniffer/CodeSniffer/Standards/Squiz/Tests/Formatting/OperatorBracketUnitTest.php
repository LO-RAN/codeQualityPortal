<?php
/**
 * Unit test class for the OperatorBracket sniff.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: OperatorBracketUnitTest.php,v 1.5 2007/01/10 05:20:52 squiz Exp $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Unit test class for the OperatorBracket sniff.
 *
 * A sniff unit test checks a .inc file for expected violations of a single
 * coding standard. Expected errors and warnings are stored in this class.
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   Release: 1.0.1
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
class Squiz_Tests_Formatting_OperatorBracketUnitTest extends AbstractSniffUnitTest
{


    /**
     * Returns the lines where errors should occur.
     *
     * The key of the array should represent the line number and the value
     * should represent the number of errors that should occur on that line.
     *
     * @return array(int => int)
     */
    public function getErrorList()
    {
        return array(
                3  => 1,
                6  => 1,
                9  => 1,
                12 => 1,
                15 => 1,
                18 => 2,
                20 => 1,
                25 => 1,
                28 => 1,
                31 => 1,
                34 => 1,
                37 => 1,
                40 => 1,
                43 => 2,
                45 => 1,
                47 => 5,
                48 => 1,
                50 => 2,
                55 => 2,
                56 => 1,
                63 => 2,
                64 => 1,
                67 => 1,
                86 => 1,
                90 => 1,
               );

    }//end getErrorList()


    /**
     * Returns the lines where warnings should occur.
     *
     * The key of the array should represent the line number and the value
     * should represent the number of warnings that should occur on that line.
     *
     * @return array(int => int)
     */
    public function getWarningList()
    {
        return array();

    }//end getWarningList()


}//end class

?>
