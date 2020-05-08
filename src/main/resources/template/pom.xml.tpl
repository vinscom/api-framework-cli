<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>[=groupId]</groupId>
    <artifactId>[=artifactId]</artifactId>
    <version>[=version]</version>
    <packaging>jar</packaging>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <argLine></argLine>
        <version.api.framework>2.5.4-SNAPSHOT</version.api.framework>
        <layer.api.framework>${settings.localRepository}/in/erail/api-framework/${version.api.framework}/api-framework-${version.api.framework}-common-config.zip</layer.api.framework>
        <#if enableAWSLambda>
        <layer.api.framework.amazon.lambda>${settings.localRepository}/in/erail/api-framework-amazon-lambda/${version.api.framework}/api-framework-amazon-lambda-${version.api.framework}-common-config.zip</layer.api.framework.amazon.lambda>
        </#if>
        <layer.[=artifactId]>${project.basedir}/config-layers/common</layer.[=artifactId]>
        <layer.[=artifactId].local>${project.basedir}/config-layers/local</layer.[=artifactId].local>
        <layer.[=artifactId].test>${project.basedir}/config-layers/test</layer.[=artifactId].test>
        <#if enableAWSLambda>
        <glue.config.lambda.service>/in/erail/amazon/lambda/service/ProxyService</glue.config.lambda.service>
        </#if>
        <#list environments as environment>
        <glue.config.[=environment].layer>./lib/api-framework-2*-common-config.zip,[=enableAWSLambda?then('./lib/api-framework-amazon-lambda-2*-common-config.zip,','')]./lib/${project.build.finalName}-common-config.zip,./lib/${project.build.finalName}-env-[=environment]-config.zip</glue.config.[=environment].layer>
        </#list>
    </properties>
    <repositories>
        <repository>
            <id>oss-sonatype-org</id>
            <name>oss.sonatype.org</name>
            <url>https://oss.sonatype.org/content/groups/public</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
    <dependencies>
        <dependency>
            <groupId>in.erail</groupId>
            <artifactId>api-framework</artifactId>
            <version>${version.api.framework}</version>
        </dependency>
        <dependency>
            <groupId>in.erail</groupId>
            <artifactId>api-framework</artifactId>
            <version>${version.api.framework}</version>
            <type>zip</type>
            <classifier>common-config</classifier>
        </dependency>
        <#if enableAWSLambda>
        <dependency>
            <groupId>in.erail</groupId>
            <artifactId>api-framework-amazon-lambda</artifactId>
            <version>${version.api.framework}</version>
            <classifier>common-config</classifier>
            <type>zip</type>
        </dependency>
        <dependency>
            <groupId>in.erail</groupId>
            <artifactId>api-framework-amazon-lambda</artifactId>
            <version>${version.api.framework}</version>
        </dependency>
        </#if>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>2.13.2</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.13.2</version>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.6.2</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>5.6.2</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>io.vertx</groupId>
            <artifactId>vertx-junit5</artifactId>
            <version>3.8.3</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
            </resource>
        </resources>
        <plugins>
            <plugin>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>3.2.0</version>
                <executions>
                    <execution>
                        <id>common-config</id>
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                        <configuration>
                            <descriptors>
                                <descriptor>src/assembly/common-config.xml</descriptor>
                            </descriptors>
                        </configuration>
                    </execution>
                    <#list environments as environment>
                    <execution>
                        <id>env-[=environment]-config</id>
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                        <configuration>
                            <descriptors>
                                <descriptor>src/assembly/env-[=environment]-config.xml</descriptor>
                            </descriptors>
                        </configuration>
                    </execution>
                    </#list>
                    <execution>
                        <id>lambda-deployment</id>
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                        <configuration>
                            <finalName>[=artifactId]</finalName>
                            <descriptors>
                                <descriptor>src/assembly/deployment.xml</descriptor>
                            </descriptors>
                            <attach>false</attach>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.22.2</version>
                <configuration>
                    <environmentVariables>
                        <LOG4J_DEFAULT_LEVEL>DEBUG</LOG4J_DEFAULT_LEVEL>
                    </environmentVariables>
                    <argLine>@{argLine} -Duser.timezone=UTC -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.Log4j2LogDelegateFactory -Dglue.layers=${layer.api.framework},[=enableAWSLambda?then('${layer.api.framework.amazon.lambda},','')]${layer.[=artifactId]},${layer.[=artifactId].local},${layer.[=artifactId].test}</argLine>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.2.0</version>
                <configuration>
                    <archive>
                        <manifest>
                            <addClasspath>true</addClasspath>
                            <classpathPrefix>./</classpathPrefix>
                            <mainClass>in.erail.glue.Boot</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>0.8.5</version>
                <executions>
                    <execution>
                        <id>pre-unit-test</id>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>post-unit-test</id>
                        <phase>test</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <release>11</release>
                    <showDeprecation>true</showDeprecation>
                </configuration>
            </plugin>
        </plugins>
    </build>
    <profiles>
        <profile>
            <id>local</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.codehaus.mojo</groupId>
                        <artifactId>exec-maven-plugin</artifactId>
                        <version>1.6.0</version>
                        <configuration>
                            <executable>java</executable>
                            <arguments>
                                <!--
                                <argument>-Xdebug</argument>
                                <argument>-Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n</argument>
                                -->
                                <argument>-Dglue.layers=${layer.api.framework},[=enableAWSLambda?then('${layer.api.framework.amazon.lambda},','')]${layer.[=artifactId]},${layer.[=artifactId].local}</argument>
                                <argument>-Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.Log4j2LogDelegateFactory</argument>
                                <argument>-Dlog4j.configurationFile=${basedir}/src/main/resources/log4j2.xml</argument>
                                <argument>-classpath</argument>
                                <classpath/>
                                <argument>in.erail.glue.Boot</argument>
                            </arguments>
                        </configuration>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>
</project>