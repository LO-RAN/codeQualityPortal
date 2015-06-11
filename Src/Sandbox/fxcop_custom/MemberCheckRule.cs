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
    public class MemberCheckRule : BaseFXCopRule
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
        public MemberCheckRule() : base("MemberCheckRule") { }

        #endregion

        public override ProblemCollection Check(Member member)
        {
            // is it a method (and not a field) ?
             Method method = member as Method;
             if (method != null)
             {
                 Problem pb = new Problem(GetResolution(method.FullName));
                 Problems.Add(pb);

                 return Problems;
             }
             else
             {
                 return null;
             }
        }
    }
}
