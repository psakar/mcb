#!/bin/bash
SRC_DIR=.
TITLES="$SRC_DIR"/src/main/java/com/chare/mcb/www/WebApplication_
#JAVA_HOME=/usr/
#/usr/java/latest/
TARGET_DIR="$SRC_DIR"/src/main/jasperreports

#ls -al "$TITLES"en.properties.xml
cp "$TITLES"en.properties.xml titles_en.xml
sed 2d -i titles_en.xml

xsltproc -o titles_en.properties titles2properties.xsl titles_en.xml 

mv titles_en.properties "$TARGET_DIR"/titles_en.properties
rm titles_en.xml
#ls -al "$TARGET_DIR"/titles_en.properties


#ls -al "$TITLES"cs.properties.xml
cp "$TITLES"cs.properties.xml titles_cs.xml
sed 2d -i titles_cs.xml

xsltproc -o titles_cs.properties titles2properties.xsl titles_cs.xml 

#"$JAVA_HOME"bin/native2ascii titles_cs_.properties > titles_cs.properties
mv titles_cs.properties "$TARGET_DIR"/titles_cs.properties
rm titles_cs.xml
#ls -al "$TARGET_DIR"/titles_cs.properties



#ls -al "$TITLES"de.properties.xml
cp "$TITLES"de.properties.xml titles_de.xml
sed 2d -i titles_de.xml

xsltproc -o titles_de.properties titles2properties.xsl titles_de.xml 

mv titles_de.properties "$TARGET_DIR"/titles_de.properties
rm titles_de.xml
#ls -al "$TARGET_DIR"/titles_de.properties



#ls -al "$TITLES"sk.properties.xml
cp "$TITLES"sk.properties.xml titles_sk.xml
sed 2d -i titles_sk.xml

xsltproc -o titles_sk.properties titles2properties.xsl titles_sk.xml 

mv titles_sk.properties "$TARGET_DIR"/titles_sk.properties
rm titles_sk.xml
#ls -al "$TARGET_DIR"/titles_sk.properties

