package org.plugin.devinfo;

import org.apache.maven.model.*;
import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.*;

import java.util.*;
import java.util.stream.*;

/**
 * Maven plugin to display developer information from the project's pom.xml.
 */
@Mojo(name = "display", defaultPhase = LifecyclePhase.VALIDATE)
public class DeveloperInfoMojo extends AbstractMojo {

    /**
     * The Maven Project object.
     * Injected by Maven itself.
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;


    /**
     * Main logic of the plugin.
     */
    @Override
    public void execute() {
        getLog().info("------------------------------------------------------------------------");
        getLog().info("                  DEVELOPER INFORMATION PLUGIN                          ");
        getLog().info("------------------------------------------------------------------------");

        List<Developer> developers = project.getDevelopers();

        if (isValid(developers)) return;

        getLog().info("Found " + developers.size() + " developer(s):");
        getLog().info("");

        showDevelopers(developers);

        getLog().info("------------------------------------------------------------------------");
        getLog().info("                  PLUGIN EXECUTION COMPLETE                             ");
        getLog().info("------------------------------------------------------------------------");
    }

    /**
     * Shows developer information if the list is valid.
     */
    private boolean isValid(List<Developer> developers) {
        if (developers == null || developers.isEmpty()) {
            getLog().warn("No developer information found in pom.xml for project: " + project.getArtifactId());
            return true;
        }
        return false;
    }

    /**
     * Displays developer information.
     */
    private void showDevelopers(List<Developer> developers) {
        int developerCount = 1;
        for (Developer developer : developers) {
            getLog().info("--- Developer " + developerCount++ + " ---");
            getLog().info("ID:          " + (developer.getId() != null ? developer.getId() : "N/A"));
            getLog().info("Name:        " + (developer.getName() != null ? developer.getName() : "N/A"));
            getLog().info("Email:       " + (developer.getEmail() != null ? developer.getEmail() : "N/A"));
            getLog().info("Organization: " + (developer.getOrganization() != null ? developer.getOrganization() : "N/A"));
            getLog().info("URL:         " + (developer.getUrl() != null ? developer.getUrl() : "N/A"));
            getLog().info("Timezone:    " + (developer.getTimezone() != null ? developer.getTimezone() : "N/A"));

            showRoles(developer);
            getLog().info("");
        }
    }

    /**
     * Displays developer roles.
     */
    private void showRoles(Developer developer) {
        if (developer.getRoles() != null && !developer.getRoles().isEmpty()) {
            String roles = developer.getRoles().stream()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.joining(", "));
            getLog().info("Roles:       " + roles);
        } else {
            getLog().info("Roles:       N/A");
        }
    }
}
