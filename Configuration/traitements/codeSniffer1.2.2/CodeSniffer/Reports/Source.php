<?php
/**
 * Source report for PHP_CodeSniffer.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Gabriele Santini <gsantini@sqli.com>
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2009 SQLI <www.sqli.com>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: IsCamelCapsTest.php 240585 2007-08-02 00:05:40Z squiz $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

/**
 * Source report for PHP_CodeSniffer.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer
 * @author    Gabriele Santini <gsantini@sqli.com>
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2009 SQLI <www.sqli.com>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   Release: 1.2.2
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
class PHP_CodeSniffer_Reports_Source implements PHP_CodeSniffer_Report
{


    /**
     * Prints the source of all errors and warnings.
     *
     * @param array   $report       Prepared report.
     * @param boolean $showWarnings Show warnings?
     * @param boolean $showSources  Show sources?
     * @param int     $width        Maximum allowed lne width.
     * 
     * @return string 
     */
    public function generate(
        $report,
        $showWarnings=true,
        $showSources=false,
        $width=80
    ) {
        $sources = array();
        $width   = max($width, 70);

        $errorsShown = 0;

        foreach ($report['files'] as $filename => $file) {
            foreach ($file['messages'] as $line => $lineErrors) {
                foreach ($lineErrors as $column => $colErrors) {
                    foreach ($colErrors as $error) {
                        $errorsShown++;

                        $source = $error['source'];
                        if (isset($sources[$source]) === false) {
                            $sources[$source] = 1;
                        } else {
                            $sources[$source]++;
                        }
                    }
                }
            }//end foreach
        }//end foreach

        if ($errorsShown === 0) {
            // Nothing to show.
            return 0;
        }

        asort($sources);
        $sources = array_reverse($sources);

        echo PHP_EOL.'PHP CODE SNIFFER VIOLATION SOURCE SUMMARY'.PHP_EOL;
        echo str_repeat('-', $width).PHP_EOL;
        if ($showSources === true) {
            echo 'SOURCE'.str_repeat(' ', ($width - 11)).'COUNT'.PHP_EOL;
            echo str_repeat('-', $width).PHP_EOL;
        } else {
            echo 'STANDARD  CATEGORY            SNIFF'.str_repeat(' ', ($width - 40)).'COUNT'.PHP_EOL;
            echo str_repeat('-', $width).PHP_EOL;
        }

        foreach ($sources as $source => $count) {
            if ($showSources === true) {
                echo $source.str_repeat(' ', ($width - 5 - strlen($source)));
            } else {
                $parts = explode('.', $source);

                if (strlen($parts[0]) > 8) {
                    $parts[0] = substr($parts[0], 0, ((strlen($parts[0]) - 8) * -1));
                }

                echo $parts[0].str_repeat(' ', (10 - strlen($parts[0])));

                $category = $this->makeFriendlyName($parts[1]);
                if (strlen($category) > 18) {
                    $category = substr($category, 0, ((strlen($category) - 18) * -1));
                }

                echo $category.str_repeat(' ', (20 - strlen($category)));

                $sniff = $this->makeFriendlyName($parts[2]);
                if (isset($parts[3]) === true) {
                    $sniff .= ' '.ucfirst($this->makeFriendlyName($parts[3]));
                }

                if (strlen($sniff) > ($width - 37)) {
                    $sniff = substr($sniff, 0, ($width - 37 - strlen($sniff)));
                }

                echo $sniff.str_repeat(' ', ($width - 35 - strlen($sniff)));
            }//end if

            echo $count.PHP_EOL;
        }//end foreach

        echo str_repeat('-', $width).PHP_EOL;
        echo 'A TOTAL OF '.$errorsShown.' SNIFF VIOLATION(S) ';
        echo 'WERE FOUND IN '.count($sources).' SOURCE(S)'.PHP_EOL;
        echo str_repeat('-', $width).PHP_EOL.PHP_EOL;

        return $errorsShown;

    }//end generate()


    /**
     * Converts a camel caps name into a readable string.
     *
     * @param string $name The camel caps name to convert.
     *
     * @return string
     */
    public function makeFriendlyName($name)
    {
        $friendlyName = '';
        $length       = strlen($name);

        $lastWasUpper   = false;
        $lastWasNumeric = false;
        for ($i = 0; $i < $length; $i++) {
            if (is_numeric($name[$i]) === true) {
                if ($lastWasNumeric === false) {
                    $friendlyName .= ' ';
                }

                $lastWasUpper   = false;
                $lastWasNumeric = true;
            } else {
                $lastWasNumeric = false;

                $char = strtolower($name[$i]);
                if ($char === $name[$i]) {
                    // Lowercase.
                    $lastWasUpper = false;
                } else {
                    // Uppercase.
                    if ($lastWasUpper === false) {
                        $friendlyName .= ' ';
                        $next = $name[($i + 1)];
                        if (strtolower($next) === $next) {
                            // Next char is lowercase so it is a word boundary.
                            $name[$i] = strtolower($name[$i]);
                        }
                    }

                    $lastWasUpper = true;
                }
            }//end if

            $friendlyName .= $name[$i];
        }//end for

        $friendlyName    = trim($friendlyName);
        $friendlyName[0] = strtoupper($friendlyName[0]);

        return $friendlyName;

    }//end makeFriendlyName()


}//end class

?>
