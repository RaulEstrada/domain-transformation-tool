package tfg.plugin;

import org.apache.maven.artifact.Artifact;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.sun.codemodel.JClassAlreadyExistsException;

import model.structure.Package;
import output.Writer;
import tfg.plugin.config.ToolConfig;
import tfg.plugin.config.TransformationOutputOption;
import tfg.plugin.utils.Utils;

import java.io.IOException;
import java.util.Set;

import javax.xml.bind.JAXBException;


@Mojo(name = "TFGTool", 
	requiresDependencyResolution = ResolutionScope.RUNTIME_PLUS_SYSTEM)
public class Tool
    extends AbstractMojo
{
	@Parameter
	private ToolConfig toolConfig;
	@Parameter(defaultValue = "${project.dependencyArtifacts}", 
			required = true, readonly = true)
	private Set<Artifact> dependencyArtifacts;
	private Utils utils = new Utils();

	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Maven TFG plugin execution started");
		String classpath = System.getProperty("java.class.path");
		for (Artifact artifact : dependencyArtifacts) {
			classpath += System.getProperty("path.separator") 
					+ artifact.getFile().getAbsolutePath();
		}
		System.setProperty("java.class.path", classpath);
		if (toolConfig != null) {
			Package model = loadModel();
			getLog().info("Maven TFG plugin: A model was loaded with " + 
					model.getAllEntitiesInPackageTree().size() + " entities, " + 
					model.getAllValueObjectsInPackageTree().size() + 
					" value objects and " + model.getAllEnumsInPackageTree().size() + 
					" custom types");
			getLog().info("Maven TFG plugin: Output directory " + 
					toolConfig.getToDirectory().getAbsolutePath());
			write(model);
		} else {
			getLog().error("Maven TFG plugin: Input configuration missing");
			throw new MojoExecutionException("Maven TFG plugin: input configuration missing");
		}
		getLog().info("Maven TFG plugin execution finished");
	}
	
	private Package loadModel() throws MojoExecutionException{
		Package intermediateModel = null;
		if (toolConfig.getDirectoryInput() != null) {
			intermediateModel = utils.getIntermediateModelFromDirectoryInput(
					toolConfig, getLog());
		} else if (toolConfig.getDatabaseInput() != null) {
			intermediateModel = utils.getIntermediateModelFromDatabaseInput(
					toolConfig, getLog());
		} else {
			getLog().error("Maven TFG plugin: No input configuration found");
			throw new MojoExecutionException("Maven TFG plugin: No input configuration found");
		}
		return intermediateModel;
	}
	
	private void write(Package model) throws MojoExecutionException {
		for (TransformationOutputOption transformation : 
			toolConfig.getConversions()) {
			getLog().info("Maven TFG plugin: Transformation detected: " + 
					transformation);
			Writer writer = utils.getWriter(transformation);
			try {
				writer.write(model, toolConfig.getToDirectory().getAbsolutePath());
				getLog().info("Maven TFG plugin: Transformation " 
						+ transformation + " processed successfully");
			} catch (ClassNotFoundException e) {
				throw new MojoExecutionException("Maven TFG plugin: An error has occurred: "
						+ e.getMessage());
			} catch (JAXBException e) {
				throw new MojoExecutionException("Maven TFG plugin: An error has occurred: "
						+ e.getMessage());
			} catch (IOException e) {
				throw new MojoExecutionException("Maven TFG plugin: An error has occurred: "
						+ e.getMessage());
			} catch (JClassAlreadyExistsException e) {
				throw new MojoExecutionException("Maven TFG plugin: An error has occurred: "
						+ e.getMessage());
			}
		}
	}
}
