<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="com.compuware.caqs.domain.dataschemas.ElementType" %>
<%@ page import="com.compuware.caqs.presentation.util.RequestUtil" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<script type="text/javascript" src="<%= request.getContextPath()%>/common/uploadFileForm.js">
</script>

<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/editors/I18NEditor.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/editors/ElementEditor.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/searches/CaqsModelEditorSearchPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/searches/CaqsModelEditorGridPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/elementPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/editors/LanguageEditorPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/panels/LanguagePanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/editors/DialecteEditorPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/panels/DialectePanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/editors/ActionPlanUnitEditorPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/panels/ActionPlanUnitPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/editors/CriterionEditorPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/panels/CriterePanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/editors/ElementTypeEditorPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/panels/ElementTypePanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/editors/GoalEditorPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/panels/GoalPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/editors/LangueEditorPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/panels/LanguePanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/editors/MetricEditorPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/panels/MetriquePanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/editors/models/ModelEditorAddMetricToCriterion.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/editors/models/ModelEditorEditAggregation.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/editors/models/ModelEditorEditAggregations.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/editors/models/ModelEditorEditFormulas.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/editors/models/ModelEditorAddElement.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/editors/models/ModelEditorGoalsCriterions.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/editors/models/ModelEditorAssociatedTools.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/editors/models/ModelEditorDefinitionPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/editors/models/ModelEditorPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/panels/ModelPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/editors/ToolEditorPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/panels/ToolPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/modeleditor.js">
</script>
