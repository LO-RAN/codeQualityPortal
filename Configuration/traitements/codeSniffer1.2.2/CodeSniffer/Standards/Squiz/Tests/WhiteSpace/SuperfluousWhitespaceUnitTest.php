<?php
/**
 * Unit test class for the SuperfluousWhitespace sniff.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: SuperfluousWhitespaceUnitTest.php 267998 2008-10-30 05:50:00Z squiz $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Unit test class for the SuperfluousWhitespace sniff.
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
 * @version   Release: 1.2.2
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
class Squiz_Tests_WhiteSpace_SuperfluousWhitespaceUnitTest extends AbstractSniffUnitTest
{


    /**
     * Returns the lines where errors should occur.
     *
     * The key of the array should represent the line number and the value
     * should represent the number of errors that should occur on that line.
     *
     * @param string $testFile The name of the file being tested.
     *
     * @return array(int => int)
     */
    public function getErrorList($testFile='SuperfluousWhitespaceUnitTest.inc')
    {
        switch ($testFile) {
        case 'SuperfluousWhitespaceUnitTest.inc':
            return array(
                    2  => 1,
                    4  => 1,
                    5  => 1,
                    6  => 1,
                    7  => 1,
                    16 => 1,
                    23 => 1,
                    30 => 1,
                    36 => 1,
                   );
            break;
        case 'SuperfluousWhitespaceUnitTest.js':
            return array(
                    1  => 1,
                    3  => 1,
                    4  => 1,
                    5  => 1,
                    6  => 1,
                    15 => 1,
                    22 => 1,
                    29 => 1,
                    35 => 1,
                   );
            break;
        case 'SuperfluousWhitespaceUnitTest.css':
            return array(
                    1  => 1,
                    8  => 1,
                    9  => 1,
                    14 => 1,
                   );
            break;
        default:
            return array();
            break;
        }//end switch

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
