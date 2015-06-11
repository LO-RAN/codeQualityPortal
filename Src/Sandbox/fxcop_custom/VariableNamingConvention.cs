using System;
using System.Collections.Generic;
using System.Text;
using Microsoft.FxCop.Sdk;
using CustomFXCopRules;
using Microsoft.FxCop.Sdk.Introspection;
using Microsoft.Cci;

namespace MyRules
{
    /*
    public class ObjVariableNamingConvention : BaseIntrospectionRule
    {


        public ObjVariableNamingConvention()
            : base("ObjVariableNamingConvention", "MyRules.Connection", typeof(ObjVariableNamingConvention).Assembly)
        {
        }
    */
        
    [CLSCompliant(false)]
    public class ObjVariableNamingConvention : BaseFXCopRule
    {
        #region Default Constructor

        public ObjVariableNamingConvention() : base("ObjVariableNamingConvention") { }

        #endregion

        public override ProblemCollection Check(Member member)
        {
            Method method = member as Method;
            bool problem = false;

            if (method != null)
            {
                InstructionList instructions = method.Instructions;
                if (instructions.Length == 0)
                    return null;

                LocalList localList = instructions[0].Value as LocalList;
                if (localList == null)
                    return null;

                Local local;

                string strName = String.Empty;
                string strType = String.Empty;

                for (int index = 0, length = localList.Length; index < length; index++)
                {
                    local = localList[index];

                    strName = local.Name.Name;
                    strType = local.Type.FullName.ToString();

                    if (strType.Equals("System.Object") && !strName.StartsWith("CS$"))
                    {
                        problem = (strName.Length < 3 || !strName.Substring(0, 3).Equals("obj"));
                    }
                    if (problem)
                    {
                        Problems.Add(new Problem(GetResolution(strName)));
                    }
                }
            }
            else
            {
                Field field = member as Field;
                if (field == null)
                    return null;
                else
                {
                    if (field.Type.FullName.ToString().Equals("System.Object"))
                    {
                        problem = ((member.Name.ToString().Length < 3 || !member.Name.ToString().Substring(0, 3).Equals("obj")));
                    }
                    if (problem)
                    {
                        Problems.Add(new Problem(GetResolution(member.Name.Name)));
                    }
                }
            }
            return Problems;
        }





    }

     [CLSCompliant(false)]
    public class StringVariableNamingConvention : BaseFXCopRule
    {
        #region Default Constructor

        public StringVariableNamingConvention() : base("StringVariableNamingConvention") { }

        #endregion




        public override ProblemCollection Check(Member member)
        {
            Method method = member as Method;

            if (method != null)
            {

                InstructionList instructions = method.Instructions;

                if (instructions.Length == 0)
                    return null;

                LocalList locals = instructions[0].Value as LocalList;

                if (locals == null)
                    return null;

                Local local;
                string strName = String.Empty;
                string strType = String.Empty;

                for (int index = 0, length = locals.Length; index < length; index++)
                {
                    local = locals[index];
                    strName = local.Name.Name;
                    strType = local.Type.FullName.ToString();

                    if (strType.Equals("System.String") && !strName.StartsWith("CS$"))
                    {
                        if (strName.Length < 1 || !strName.Substring(0, 3).Equals("str"))
                        {
                            Problems.Add(new Problem(GetResolution(strName)));
                        }
                    }
                }
            }
            else
            {
                Field field = member as Field;

                if (field == null)
                    return null;

                else
                {

                    if (field.Type.FullName.ToString().Equals("System.Boolean"))
                    {
                        if (member.Name.ToString().Length < 2 || !member.Name.ToString().Substring(0, 1).Equals("b"))
                        {
                            Problems.Add(new Problem(GetResolution(member.Name.Name)));
                        }
                    }
                }
            }

            return Problems;
        }
    }



     [CLSCompliant(false)]
    public class BoolVariableNamingConvention : BaseFXCopRule
    {
        #region Default Constructor

        public BoolVariableNamingConvention() : base("BoolVariableNamingConvention") { }

        #endregion


        public override ProblemCollection Check(Member member)
        {
            Method method = member as Method;

            if (method != null)
            {

                InstructionList instructions = method.Instructions;

                if (instructions.Length == 0)
                    return null;

                LocalList locals = instructions[0].Value as LocalList;

                if (locals == null)
                    return null;

                Local local;
                string strName = String.Empty;
                string strType = String.Empty;

                for (int index = 0, length = locals.Length; index < length; index++)
                {
                    local = locals[index];
                    strName = local.Name.Name;
                    strType = local.Type.FullName.ToString();

                    if (strType.Equals("System.Boolean") && !strName.StartsWith("CS$"))
                    {
                        if (strName.Length < 1 || !strName.Substring(0, 1).Equals("b"))
                        {
                            Problems.Add(new Problem(GetResolution(strName)));
                        }
                    }
                }
            }
            else
            {
                Field field = member as Field;

                if (field == null)
                    return null;

                else
                {

                    if (field.Type.FullName.ToString().Equals("System.Boolean"))
                    {
                        if (member.Name.ToString().Length < 2 || !member.Name.ToString().Substring(0, 1).Equals("b"))
                        {
                            Problems.Add(new Problem(GetResolution(member.Name.Name)));
                        }
                    }
                }
            }

            return Problems;
        }
    }



     [CLSCompliant(false)]
    public class IntVariableNamingConvention : BaseFXCopRule
    {
        #region Default Constructor

        public IntVariableNamingConvention() : base("IntVariableNamingConvention") { }

        #endregion

        public override ProblemCollection Check(Member member)
        {
            Method method = member as Method;
            bool problem = false;

            if (method != null)
            {
                InstructionList instructions = method.Instructions;
                if (instructions.Length == 0)
                    return null;

                LocalList localList = instructions[0].Value as LocalList;
                if (localList == null)
                    return null;

                Local local;
                string strName = String.Empty;
                string strType = String.Empty;

                for (int index = 0, length = localList.Length; index < length; index++)
                {
                    local = localList[index];

                    strName = local.Name.Name;
                    strType = local.Type.FullName.ToString();

                    if (strType.Equals("System.Int32") && !strName.StartsWith("CS$"))
                    {
                        problem = (strName.Length < 1 || !strName.Substring(0, 1).Equals("i"));
                    }
                    if (problem)
                    {
                        Problems.Add(new Problem(GetResolution(strName)));
                    }
                }
            }
            else
            {

                Field field = member as Field;

                if (field == null)
                {
                    return null;
                }
                else
                {
                    if (field.Type.FullName.ToString().Equals("System.Int32") && !member.Name.Name.StartsWith("CS$"))
                    {
                        problem = ((member.Name.ToString().Length < 1 || !member.Name.ToString().Substring(0, 1).Equals("i")));
                    }
                    if (problem)
                    {
                        Problems.Add(new Problem(GetResolution(member.Name.Name)));
                    }
                }
            }

            return Problems;

        }


    }
}

