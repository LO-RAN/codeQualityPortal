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
using Microsoft.Cci;
using Microsoft.FxCop.Sdk.Introspection;

#endregion

namespace com.compuware.fxcop.rules
{
    [CLSCompliant(false)]
    public class MemberDependencyCheckRule : BaseFXCopRule
    {
        #region Default Constructor

        public MemberDependencyCheckRule() : base("MemberDependencyCheckRule") { }

        #endregion

        public override ProblemCollection Check(Member member)
        {
            // is it a method (and not a field) ?
             Method method = member as Method;
             if (method != null)
             {
                 MethodList ml=CallGraph.CallersFor(method);
                 for (int i = 0; i<ml.Length; i++)
                 {
                     Problem pb = new Problem(GetResolution(ml[i].GetFullUnmangledNameWithTypeParameters()));
                     Problems.Add(pb);
                 }

                 return Problems;
             }
             else
             {
                 return null;
             }
        }
    }
}
