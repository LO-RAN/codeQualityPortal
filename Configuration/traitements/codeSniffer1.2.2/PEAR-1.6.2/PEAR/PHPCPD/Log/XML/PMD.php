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

require 'PHPCPD/Log/XML.php';

/**
 * Implementation of PHPCPD_Log_XML that writes in PMD-CPD format.
 *
 * @author    Sebastian Bergmann <sb@sebastian-bergmann.de>
 * @copyright 2009 Sebastian Bergmann <sb@sebastian-bergmann.de>
 * @license   http://www.opensource.org/licenses/bsd-license.php  BSD License
 * @version   Release: 1.2.0
 * @link      http://github.com/sebastianbergmann/phpcpd/tree
 * @since     Class available since Release 1.0.0
 */
class PHPCPD_Log_XML_PMD extends PHPCPD_Log_XML
{
    /**
     * Processes a list of clones.
     *
     * @param PHPCPD_CloneMap $clones
     */
    public function processClones(PHPCPD_CloneMap $clones)
    {
        $cpd = $this->document->createElement('pmd-cpd');
        $cpd->setAttribute('version', 'phpcpd 1.2.0');
        $this->document->appendChild($cpd);

        foreach ($clones as $clone) {
            $duplication = $cpd->appendChild(
              $this->document->createElement('duplication')
            );

            $duplication->setAttribute('lines', $clone->size);
            $duplication->setAttribute('tokens', $clone->tokens);

            $file = $duplication->appendChild(
              $this->document->createElement('file')
            );

            $file->setAttribute('path', $clone->aFile);
            $file->setAttribute('line', $clone->aStartLine);

            $file = $duplication->appendChild(
              $this->document->createElement('file')
            );

            $file->setAttribute('path', $clone->bFile);
            $file->setAttribute('line', $clone->bStartLine);

            $duplication->appendChild(
              $this->document->createElement(
                'codefragment',
                htmlspecialchars(
                  $this->convertToUtf8($clone->getLines()),
                  ENT_COMPAT,
                  'UTF-8'
                )
              )
            );
        }

        $this->flush();
    }
}
?>
