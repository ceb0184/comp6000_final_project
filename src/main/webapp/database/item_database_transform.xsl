<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">

	<xsl:output method="html"
	            doctype-system="about:legacy-compat"
	            encoding="UTF-8"
	            indent="yes"/>
	
	<xsl:template match="/">
		<html>
			<head>
				<title>Store Page</title>
				<link rel="stylesheet" href="http://localhost:8080/final_project/store_style.css"/>
			</head>
			<body>
				<form action="cartServlet" method="post">
					<xsl:for-each select="items/item">
						<table class="styled-table">
							<xsl:variable name="name" select="name"/>
							<xsl:variable name="description" select="description"/>
							<xsl:variable name="price" select="price"/>
							<thead>
								<tr>
									<th>Picture</th>
									<th>Title</th>
									<th>Description</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td class="imageCell">
										<img class="image" src="{image}" alt="Logo" height="500" width="500"/>
									</td>
									<td class="name"><xsl:value-of select="$name"/></td>
									<td class="description">
										<xsl:value-of select="$description"/><br></br><br></br>
										$<xsl:value-of select="$price"/>
									</td>
									<td class="check">
										<input type="checkbox" class="largerCheckbox" name="gameSelect" value="{name}|{price}"/>
										<label for="gameSelect">Add game to cart</label>
									</td>
								</tr>
							</tbody>
							
						</table>
					</xsl:for-each>
					<input type="submit" value="Place Order!" class="form-submit-button"/>
				</form>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
