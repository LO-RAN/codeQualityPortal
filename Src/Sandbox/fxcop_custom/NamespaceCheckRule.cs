#region History
/*
 ************************************************************************ 
 * HISTORY
 ************************************************************************
 * Name          Date       ID    Changes
 * ============= ========== ===== =======================================
 * Laurent IZAC  2010-12-22       Created.
 ************************************************************************
 */
#endregion

#region .Net Framework Base class Imports

using System;
using System.Text;

#endregion

#region Custom Using / Imports

using Microsoft.FxCop.Sdk;
using CustomFXCopRules.Globals;
using CustomFXCopRules;
using Microsoft.FxCop.Sdk.Introspection;
using Microsoft.Cci;

#endregion

namespace com.compuware.fxcop.rules
{
    /// <summary>
    /// An empty rule that can be used as a template for defining 
    /// other rules.
    /// </summary>
    [CLSCompliant(false)]
    public class NamespaceCheckRule : BaseFXCopRule
    {
        #region Default Constructor

        /// <summary>
        /// Default constructor that is required otherwise an error is
        /// idplsyaed by FXCop when loading the rule for the first time.
        /// </summary>
        /// <remarks>The base("ExampleRule") is also required
        /// and will cause FXCop to lookup the "ExampleRule"
        /// resource data for this rule in the embedded resource. This
        /// embedded resource is usually the 'rules.xml' file.</remarks>
        public NamespaceCheckRule() : base("NamespaceCheckRule") { }

        #endregion

        public override ProblemCollection  Check(string namespaceName, TypeNodeList types)
        {
            Problem pb = new Problem(GetResolution(namespaceName));
            Problems.Add(pb);
            return Problems;
        }
    }
}
