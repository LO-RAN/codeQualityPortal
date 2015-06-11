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

#region .Net Framework Base Library Imports

using System;
using System.Text;

#endregion

#region Custom Namespace Imports

using Microsoft.FxCop.Sdk;
using Microsoft.FxCop.Sdk.Introspection;
using CustomFXCopRules.Globals;

#endregion

namespace CustomFXCopRules
{
    /// <summary>
    /// The base class for all of our custom FXCop rules
    /// </summary>
    /// <remarks></remarks>
    [CLSCompliant(false)]
    public abstract class BaseFXCopRule : BaseIntrospectionRule
    {
        #region Constructors

        public BaseFXCopRule() : this("BaseFXCopRule") { }

        public BaseFXCopRule(string name) : base(name, Constants.RESOURCENAME_RULES , typeof(BaseFXCopRule).Assembly) { }

        #endregion
    }
}
