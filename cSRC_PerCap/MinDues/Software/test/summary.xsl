<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html"/>

<xsl:variable name="testCount" select="sum(testsuites/testsuite/@tests)"/>
<xsl:variable name="errorCount" select="sum(testsuites/testsuite/@errors)"/>
<xsl:variable name="failureCount" select="sum(testsuites/testsuite/@failures)"/>
<xsl:variable name="timeCount" select="sum(testsuites/testsuite/@time)"/>
<xsl:variable name="goodCount" select="($testCount - $failureCount - $errorCount)"/>
<xsl:variable name="successRate" select="$goodCount div $testCount"/>

<xsl:template match="/">
<font>
<xsl:attribute name="color">
	<xsl:choose>
		<xsl:when test="$successRate=1">green</xsl:when>
		<xsl:otherwise>red</xsl:otherwise>
	</xsl:choose>
</xsl:attribute>
	 <xsl:value-of select="$goodCount"/>/<xsl:value-of select="$testCount"/>
	(<xsl:value-of select="format-number($successRate,'0.00%')"/>)
</font>

</xsl:template>

</xsl:stylesheet>

