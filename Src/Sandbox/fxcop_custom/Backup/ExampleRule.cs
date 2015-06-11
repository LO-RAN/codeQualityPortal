#region History
/*
 ************************************************************************ 
 * HISTORY
 ************************************************************************
 * Name          Date       ID    Changes
 * ============= ========== ===== =======================================
 * Paul Glavich  14-07-2005       Created.
 ************************************************************************
 */
#endregion

#region .Net Framework Base class Imports

using System;
using System.Text;

#endregion

#region Custom Using / Imports

using Microsoft.FxCop.Sdk;
using Microsoft.FxCop.Sdk.Introspection;
using CustomFXCopRules.Globals;

#endregion

namespace CustomFXCopRules
{
    /// <summary>
    /// An empty rule that can be used as a template for defining 
    /// other rules.
    /// </summary>
    [CLSCompliant(false)]
    public class ExampleRule : BaseFXCopRule
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
        public ExampleRule() : base("ExampleRule") { }

        #endregion

        public override ProblemCollection Check(string namespaceName, Microsoft.Cci.TypeNodeList types)
        {
            bool failed = true;

            if (namespaceName.Length < Constants.COMPANY_NAME.Length)
            {
                failed = true;
            }
            else
            {
                int pos = namespaceName.IndexOf('.');
                if (pos >= 0)
                {
                    string nspaceFirstPart = namespaceName.Substring(0, pos);
                    if (nspaceFirstPart == Constants.COMPANY_NAME)
                    {
                        failed = false;
                    }
                }
            }
            if (failed)
            {
                Problems.Add(new Problem(GetResolution(namespaceName)));
                return Problems;
            }
            else
            {
                return null;
            }            
        }
    }
}
