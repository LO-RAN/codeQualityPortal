<?php
/**
 * phpcpd
 *
 * Copyright (c) 2009, Sebastian Bergmann <sb@sebastian-bergmann.de>.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *
 *   * Neither the name of Sebastian Bergmann nor the names of his
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * @package   phpcpd
 * @author    Sebastian Bergmann <sb@sebastian-bergmann.de>
 * @copyright 2009 Sebastian Bergmann <sb@sebastian-bergmann.de>
 * @license   http://www.opensource.org/licenses/bsd-license.php  BSD License
 * @since     File available since Release 1.0.0
 */

/**
 * Command-line options parsing class.
 *
 * @author    Andrei Zmievski <andrei@php.net>
 * @author    Sebastian Bergmann <sb@sebastian-bergmann.de>
 * @copyright 2009 Sebastian Bergmann <sb@sebastian-bergmann.de>
 * @license   http://www.opensource.org/licenses/bsd-license.php  BSD License
 * @version   Release: 1.2.0
 * @link      http://github.com/sebastianbergmann/phpcpd/tree
 * @since     Class available since Release 1.0.0
 */
class PHPCPD_TextUI_Getopt
{
    public static function getopt(array $args, $short_options, $long_options = NULL)
    {
        if (empty($args)) {
            return array(array(), array());
        }

        $opts     = array();
        $non_opts = array();

        if ($long_options) {
            sort($long_options);
        }

        if (isset($args[0]{0}) && $args[0]{0} != '-') {
            array_shift($args);
        }

        reset($args);

        while (list($i, $arg) = each($args)) {
            if ($arg == '--') {
                $non_opts = array_merge($non_opts, array_slice($args, $i + 1));
                break;
            }

            if ($arg{0} != '-' || (strlen($arg) > 1 && $arg{1} == '-' && !$long_options)) {
                $non_opts = array_merge($non_opts, array_slice($args, $i));
                break;
            }

            elseif (strlen($arg) > 1 && $arg{1} == '-') {
                self::parseLongOption(substr($arg, 2), $long_options, $opts, $args);
            }

            else {
                self::parseShortOption(substr($arg, 1), $short_options, $opts, $args);
            }
        }

        return array($opts, $non_opts);
    }

    protected static function parseShortOption($arg, $short_options, &$opts, &$args)
    {
        $argLen = strlen($arg);

        for ($i = 0; $i < $argLen; $i++) {
            $opt = $arg{$i};
            $opt_arg = NULL;

            if (($spec = strstr($short_options, $opt)) === FALSE || $arg{$i} == ':') {
                throw new RuntimeException("unrecognized option -- $opt");
            }

            if (strlen($spec) > 1 && $spec{1} == ':') {
                if (strlen($spec) > 2 && $spec{2} == ':') {
                    if ($i + 1 < $argLen) {
                        $opts[] = array($opt, substr($arg, $i + 1));
                        break;
                    }
                } else {
                    if ($i + 1 < $argLen) {
                        $opts[] = array($opt, substr($arg, $i + 1));
                        break;
                    }

                    else if (list(, $opt_arg) = each($args)) {
                    }

                    else {
                        throw new RuntimeException("option requires an argument -- $opt");
                    }
                }
            }

            $opts[] = array($opt, $opt_arg);
        }
    }

    protected static function parseLongOption($arg, $long_options, &$opts, &$args)
    {
        @list($opt, $opt_arg) = explode('=', $arg);
        $opt_len = strlen($opt);

        for ($i = 0; $i < count($long_options); $i++) {
            $long_opt  = $long_options[$i];
            $opt_start = substr($long_opt, 0, $opt_len);

            if ($opt_start != $opt) continue;

            $opt_rest = substr($long_opt, $opt_len);

            if ($opt_rest != '' && $opt{0} != '=' &&
                $i + 1 < count($long_options) &&
                $opt == substr($long_options[$i+1], 0, $opt_len)) {
                throw new RuntimeException("option --$opt is ambiguous");
            }

            if (substr($long_opt, -1) == '=') {
                if (substr($long_opt, -2) != '==') {
                    if (!strlen($opt_arg) && !(list(, $opt_arg) = each($args))) {
                        throw new RuntimeException("option --$opt requires an argument");
                    }
                }
            }

            else if ($opt_arg) {
                throw new RuntimeException("option --$opt doesn't allow an argument");
            }

            $opts[] = array('--' . $opt, $opt_arg);
            return;
        }

        throw new RuntimeException("unrecognized option --$opt");
    }
}
?>
