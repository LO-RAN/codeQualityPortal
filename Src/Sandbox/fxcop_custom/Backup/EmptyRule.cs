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

#endregion

namespace CustomFXCopRules
{
    /// <summary>
    /// An empty rule that can be used as a template for defining 
    /// other rules.
    /// </summary>
    [CLSCompliant(false)]
    public class EmptyRule : BaseFXCopRule
    {
        #region Default Constructor

        /// <summary>
        /// Default constructor that is required otherwise an error is
        /// idplsyaed by FXCop when loading the rule for the first time.
        /// </summary>
        /// <remarks>The base("EmptyRule") is also required
        /// and will cause FXCop to lookup the "EmptyRule"
        /// resource data for this rule in the embedded resource. This
        /// embedded resource is usually the 'rules.xml' file.</remarks>
        public EmptyRule() : base("EmptyRule") { }

        #endregion

        public override ProblemCollection Check(Microsoft.Cci.Member member)
        {
            return null;
            //Problems.Add(new Problem(GetResolution("SomeArgument")));
            //return Problems;
        }
    }
}
