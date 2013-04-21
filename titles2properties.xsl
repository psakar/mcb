<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text" encoding="utf-8"/>

<xsl:variable name="crlf" select="'&#13;&#10;'"/>

<!-- root element -->
<xsl:template match="/properties">
<xsl:for-each select="entry">
	<xsl:value-of select="@key"/>=<xsl:value-of select="."/><xsl:value-of select="$crlf"/>
</xsl:for-each>
</xsl:template>
</xsl:stylesheet>
