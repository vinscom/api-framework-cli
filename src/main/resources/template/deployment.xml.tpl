<?xml version="1.0" encoding="UTF-8"?>
<assembly xmlns="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.2"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.2 http://maven.apache.org/xsd/assembly-1.1.2.xsd">
    <id>deployment</id>
    <formats>
        <format>dir</format>
    </formats>
    <includeBaseDirectory>false</includeBaseDirectory>
    <fileSets>
        <fileSet>
            <outputDirectory>/lib</outputDirectory>
            <directory>${project.build.directory}</directory>
            <includes>
                <include>${project.build.finalName}-common-config.zip</include>
<#list envs as e>
                <include>${project.build.finalName}-env-[=e]-config.zip</include>
</#list>
            </includes>
        </fileSet>
        <fileSet>
            <outputDirectory>/</outputDirectory>
            <directory>${project.basedir}/src/main/resources</directory>
            <includes>
                <include>*.config</include>
                <include>*.sh</include>
            </includes>
            <filtered>true</filtered>
        </fileSet>
        <fileSet>
            <outputDirectory>/</outputDirectory>
            <directory>${project.basedir}/src/main/resources</directory>
            <includes>
                <include>log4j2.xml</include>
            </includes>
        </fileSet>
    </fileSets>
    <dependencySets>
        <dependencySet>
            <outputDirectory>/lib</outputDirectory>
        </dependencySet>
    </dependencySets>
</assembly>
