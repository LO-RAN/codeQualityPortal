<?php
/**
 * Unit test class for the FunctionClosingBraceSpace sniff.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @author    Marc McIntyre <mmcintyre@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: FunctionClosingBraceSpaceUnitTest.php 267848 2008-10-27 02:16:59Z squiz $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Unit test class for the FunctionClosingBraceSpace sniff.
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
class Squiz_Tests_WhiteSpace_FunctionClosingBraceSpaceUnitTest extends AbstractSniffUnitTest
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
    public function getErrorList($testFile='FunctionClosingBraceSpaceUnitTest.inc')
    {
        switch ($testFile) {
        case 'FunctionClosingBraceSpaceUnitTest.inc':
            return array(
                    10 => 1,
                    21 => 1,
                   );
            break;
        case 'FunctionClosingBraceSpaceUnitTest.js':
            return array(
                    13 => 1,
                    25 => 1,
                    32 => 1,
                    53 => 1,
                    59 => 1,
                    67 => 1,
                    84 => 1,
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
