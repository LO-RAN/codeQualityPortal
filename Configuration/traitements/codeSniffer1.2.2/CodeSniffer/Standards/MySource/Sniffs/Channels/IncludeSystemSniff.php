<?php
/**
 * Ensures that systems, asset types and libs are included before they are used.
 *
 * PHP version 5
 *
 * @category  PHP
 * @package   PHP_CodeSniffer_MySource
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   CVS: $Id: IncludeSystemSniff.php 293522 2010-01-13 22:28:20Z squiz $
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */

if (class_exists('PHP_CodeSniffer_Standards_AbstractScopeSniff', true) === false) {
    $error = 'Class PHP_CodeSniffer_Standards_AbstractScopeSniff not found';
    throw new PHP_CodeSniffer_Exception($error);
}

/**
 * Ensures that systems, asset types and libs are included before they are used.
 *
 * @category  PHP
 * @package   PHP_CodeSniffer_MySource
 * @author    Greg Sherwood <gsherwood@squiz.net>
 * @copyright 2006 Squiz Pty Ltd (ABN 77 084 670 600)
 * @license   http://matrix.squiz.net/developer/tools/php_cs/licence BSD Licence
 * @version   Release: 1.2.2
 * @link      http://pear.php.net/package/PHP_CodeSniffer
 */
class MySource_Sniffs_Channels_IncludeSystemSniff extends PHP_CodeSniffer_Standards_AbstractScopeSniff
{

    /**
     * A list of classes that don't need to be included.
     *
     * @var array(string)
     */
    private $_ignore = array(
                        'self',
                        'parent',
                        'channels',
                        'basesystem',
                        'dal',
                        'init',
                        'pdo',
                        'util',
                        'ziparchive',
                        'phpunit_framework_assert',
                        'abstractmysourceunittest',
                        'abstractdatacleanunittest',
                        'exception',
                        'abstractwidgetwidgettype',
						'sfconfig'//pour symfony
                       );


    /**
     * Constructs a Squiz_Sniffs_Scope_MethodScopeSniff.
     */
    public function __construct()
    {
        parent::__construct(array(T_FUNCTION), array(T_DOUBLE_COLON, T_EXTENDS), true);

    }//end __construct()


    /**
     * Processes the function tokens within the class.
     *
     * @param PHP_CodeSniffer_File $phpcsFile The file where this token was found.
     * @param integer              $stackPtr  The position where the token was found.
     * @param integer              $currScope The current scope opener token.
     *
     * @return void
     */
    protected function processTokenWithinScope(
        PHP_CodeSniffer_File $phpcsFile,
        $stackPtr,
        $currScope
    ) {
        $tokens = $phpcsFile->getTokens();

        // Determine the name of the class that the static function
        // is being called on.
        $classNameToken = $phpcsFile->findPrevious(
            T_WHITESPACE,
            ($stackPtr - 1),
            null,
            true
        );

        $className = $tokens[$classNameToken]['content'];
        if (in_array(strtolower($className), $this->_ignore) === true) {
            return;
        }

        $includedClasses = array();

        $fileName = strtolower($phpcsFile->getFilename());
        $matches  = array();
        if (preg_match('|/systems/(.*)/([^/]+)?actions.inc$|', $fileName, $matches) !== 0) {
            // This is an actions file, which means we don't
            // have to include the system in which it exists
            // We know the system from the path.
            $includedClasses[] = $matches[2];
        }

        // Go searching for includeSystem and includeAsset calls within this
        // function, or the inclusion of .inc files, which
        // would be library files.
        for ($i = ($currScope + 1); $i < $stackPtr; $i++) {
            $name = $this->getIncludedClassFromToken($phpcsFile, $tokens, $i);
            if ($name !== false) {
                $includedClasses[] = $name;
                // Special case for Widgets cause they are, well, special.
            } else if (strtolower($tokens[$i]['content']) === 'includewidget') {
                $typeName          = $phpcsFile->findNext(T_CONSTANT_ENCAPSED_STRING, ($i + 1));
                $typeName          = trim($tokens[$typeName]['content'], " '");
                $includedClasses[] = strtolower($typeName).'widgettype';
            }
        }

        // Now go searching for includeSystem, includeAsset or require/include
        // calls outside our scope. If we are in a class, look outside the
        // class. If we are not, look outside the function.
        $condPtr = $currScope;
        if ($phpcsFile->hasCondition($stackPtr, T_CLASS) === true) {
            foreach ($tokens[$stackPtr]['conditions'] as $condPtr => $condType) {
                if ($condType === T_CLASS) {
                    break;
                }
            }
        }

        for ($i = 0; $i < $condPtr; $i++) {
            // Skip other scopes.
            if (isset($tokens[$i]['scope_closer']) === true) {
                $i = $tokens[$i]['scope_closer'];
                continue;
            }

            $name = $this->getIncludedClassFromToken($phpcsFile, $tokens, $i);
            if ($name !== false) {
                $includedClasses[] = $name;
            }
        }//end for

        // If we are in a testing class, we might have also included
        // some systems and classes in our setUp() method.
        $setupFunction = null;
        if ($phpcsFile->hasCondition($stackPtr, T_CLASS) === true) {
            foreach ($tokens[$stackPtr]['conditions'] as $condPtr => $condType) {
                if ($condType === T_CLASS) {
                    // Is this is a testing class?
                    $name = $phpcsFile->findNext(T_STRING, $condPtr);
                    $name = $tokens[$name]['content'];
                    if (substr($name, -8) === 'UnitTest') {
                        // Look for a method called setUp().
                        $end      = $tokens[$condPtr]['scope_closer'];
                        $function = $phpcsFile->findNext(T_FUNCTION, ($condPtr + 1), $end);
                        while ($function !== false) {
                            $name = $phpcsFile->findNext(T_STRING, $function);
                            if ($tokens[$name]['content'] === 'setUp') {
                                $setupFunction = $function;
                                break;
                            }

                            $function = $phpcsFile->findNext(T_FUNCTION, ($function + 1), $end);
                        }
                    }
                }
            }//end foreach
        }//end if

        if ($setupFunction !== null) {
            $start = ($tokens[$setupFunction]['scope_opener'] + 1);
            $end   = $tokens[$setupFunction]['scope_closer'];
            for ($i = $start; $i < $end; $i++) {
                $name = $this->getIncludedClassFromToken($phpcsFile, $tokens, $i);
                if ($name !== false) {
                    $includedClasses[] = $name;
                }
            }
        }//end if

        if (in_array(strtolower($className), $includedClasses) === false) {
            $error = "Static method called on non-included class or system \"$className\"; include system with Channels::includeSystem() or include class with require_once";
            $phpcsFile->addError($error, $stackPtr);
        }

    }//end processTokenWithinScope()


    /**
     * Processes a token within the scope that this test is listening to.
     *
     * @param PHP_CodeSniffer_File $phpcsFile The file where the token was found.
     * @param int                  $stackPtr  The position in the stack where
     *                                        this token was found.
     *
     * @return void
     */
    protected function processTokenOutsideScope(PHP_CodeSniffer_File $phpcsFile, $stackPtr)
    {
        $tokens = $phpcsFile->getTokens();

        if ($tokens[$stackPtr]['code'] === T_EXTENDS) {
            // Find the class name.
            $classNameToken = $phpcsFile->findNext(T_STRING, ($stackPtr + 1));
            $className      = $tokens[$classNameToken]['content'];
        } else {
            // Determine the name of the class that the static function
            // is being called on.
            $classNameToken = $phpcsFile->findPrevious(T_WHITESPACE, ($stackPtr - 1), null, true);
            $className      = $tokens[$classNameToken]['content'];
        }

        // Some systems are always available.
        if (in_array(strtolower($className), $this->_ignore) === true) {
            return;
        }

        $includedClasses = array();

        $fileName = strtolower($phpcsFile->getFilename());
        $matches  = array();
        if (preg_match('|/systems/([^/]+)/([^/]+)?actions.inc$|', $fileName, $matches) !== 0) {
            // This is an actions file, which means we don't
            // have to include the system in which it exists
            // We know the system from the path.
            $includedClasses[] = $matches[1];
        }

        // Go searching for includeSystem, includeAsset or require/include
        // calls outside our scope.
        for ($i = 0; $i < $stackPtr; $i++) {
            // Skip classes and functions as will we never get
            // into their scopes when including this file, although
            // we have a chance of getting into IF's, WHILE's etc.
            $ignoreTokens = array(
                             T_CLASS,
                             T_INTERFACE,
                             T_FUNCTION,
                            );

            if (in_array($tokens[$i]['code'], $ignoreTokens) === true
                && isset($tokens[$i]['scope_closer']) === true
            ) {
                $i = $tokens[$i]['scope_closer'];
                continue;
            }

            $name = $this->getIncludedClassFromToken($phpcsFile, $tokens, $i);
            if ($name !== false) {
                $includedClasses[] = $name;
                // Special case for Widgets cause they are, well, special.
            } else if (strtolower($tokens[$i]['content']) === 'includewidget') {
                $typeName          = $phpcsFile->findNext(T_CONSTANT_ENCAPSED_STRING, ($i + 1));
                $typeName          = trim($tokens[$typeName]['content'], " '");
                $includedClasses[] = strtolower($typeName).'widgettype';
            }
        }//end for

        if (in_array(strtolower($className), $includedClasses) === false) {
            if ($tokens[$stackPtr]['code'] === T_EXTENDS) {
                $error = "Class extends non-included class or system \"$className\"; include system with Channels::includeSystem() or include class with require_once";
            } else {
                $error = "Static method called on non-included class or system \"$className\"; include system with Channels::includeSystem() or include class with require_once";
            }

            $phpcsFile->addError($error, $stackPtr);
        }

    }//end processTokenOutsideScope()


    /**
     * Determines the included class name from given token.
     *
     * @param PHP_CodeSniffer_File $phpcsFile The file where this token was found.
     * @param array                $tokens    The array of file tokens.
     * @param int                  $stackPtr  The position in the tokens array of the
     *                                        potentially included class.
     *
     * @return string
     */
    protected function getIncludedClassFromToken(
        PHP_CodeSniffer_File $phpcsFile,
        array $tokens,
        $stackPtr
    ) {
        if (strtolower($tokens[$stackPtr]['content']) === 'includesystem') {
            $systemName = $phpcsFile->findNext(T_CONSTANT_ENCAPSED_STRING, ($stackPtr + 1));
            $systemName = trim($tokens[$systemName]['content'], " '");
            return strtolower($systemName);
        } else if (strtolower($tokens[$stackPtr]['content']) === 'includeasset') {
            $typeName = $phpcsFile->findNext(T_CONSTANT_ENCAPSED_STRING, ($stackPtr + 1));
            $typeName = trim($tokens[$typeName]['content'], " '");
            return strtolower($typeName).'assettype';
        } else if (in_array($tokens[$stackPtr]['code'], PHP_CodeSniffer_Tokens::$includeTokens) === true) {
            $filePath = $phpcsFile->findNext(T_CONSTANT_ENCAPSED_STRING, ($stackPtr + 1));
            $filePath = $tokens[$filePath]['content'];
            $filePath = trim($filePath, " '");
            $filePath = basename($filePath, '.inc');
            return strtolower($filePath);
        }

        return false;

    }//end getIncludedClassFromToken()


}//end class

?>
