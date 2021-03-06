<#ftl strip_whitespace=true>
<#macro renderStat stats name description class=""><#assign value = stats.get(name)!"N/A"><#if (value != "0")><span class="${class}">${description} ${value}</span><#else>${description} ${value}</#if></#macro>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
  <title>JBehave Reports</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link rel="stylesheet" href="styles/jbehave-reports.css" type="text/css" />
  <link rel="stylesheet" href="styles/shCore.css" type="text/css" />
  <link rel="stylesheet" href="styles/shThemeDefault.css" type="text/css" />
  <script src="scripts/shCore.js" type="text/javascript"></script>
  <script src="scripts/shBrushXml.js" type="text/javascript"></script>
  <script src="scripts/shBrushPlain.js" type="text/javascript"></script>
</head>

<body>
<div id="banner"><img src="images/jbehave-logo.png" alt="jbehave" />
<div class="clear"></div>
</div>

<div class="reports">

<h2>Story Reports</h2>

<table>
<tr><th>Name</th><th>Statistics</th><th>View</th></tr>
<#list reportsTable.getReports() as report>
<#assign filesByFormat = report.filesByFormat>
<tr>
<td>${report.name}</td>
<td>
<#assign stats = report.asProperties("stats")>
<#if (stats.size() > 0)>
<@renderStat stats "scenarios" "Scenarios:" "successful"/> (<@renderStat stats "scenariosFailed" "Failed:" "failed" />)<br/>
<@renderStat stats "steps" "Steps:" /> (<@renderStat stats "stepsSuccessful" "Successful:" "successful"/>; <@renderStat stats "stepsPending" "Pending:" "pending"/>; <@renderStat stats "stepsNotPerformed" "Not Performed:" "notPerformed" />; <@renderStat stats "stepsFailed" "Failed:" "failed"/>; <@renderStat stats "stepsIgnorable" "Ignorable:" "ignorable"/>;)<br/>
<#else>
N/A
</#if>
</td>
<td><#list filesByFormat.keySet() as format><#assign file = filesByFormat.get(format)><a href="${file.name}">${format}</a><#if format_has_next>|</#if></#list></td>
</tr>
</#list>
</table>
<br />
</div>

<div class="clear"></div>
<div id="footer">
<div class="left">Generated at ${date?string("dd/MM/yyyy HH:mm:ss")}</div>
<div class="right">JBehave 3.x &#169; 2003-2010</div>
<div class="clear"></div>
</div>

<script type="text/javascript">
  SyntaxHighlighter.all()
</script>
</body>
</html>
