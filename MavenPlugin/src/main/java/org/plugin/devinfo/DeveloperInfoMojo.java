package org.plugin.devinfo;

import org.apache.commons.collections4.*;
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
        if (developers == null || developers.isEmpty()) {
            getLog().info("No developers information available");
            return;
        }

        int developerCount = 1;
        for (Developer developer : developers) {
            getLog().info("--- Developer " + developerCount++ + " ---");
            getLog().info("ID:          " + Optional.ofNullable(developer.getId()).orElse("N/A"));
            getLog().info("Name:        " + Optional.ofNullable(developer.getName()).orElse("N/A"));
            getLog().info("Email:       " + Optional.ofNullable(developer.getEmail()).orElse("N/A"));
            getLog().info("URL:         " + Optional.ofNullable(developer.getUrl()).orElse("N/A"));
            getLog().info("Organization: " + Optional.ofNullable(developer.getOrganization()).orElse("N/A"));
            getLog().info("Org URL:     " + Optional.ofNullable(developer.getOrganizationUrl()).orElse("N/A"));
            getLog().info("Timezone:    " + Optional.ofNullable(developer.getTimezone()).orElse("N/A"));

            showRoles(developer);
            showProperties(developer);
            getLog().info("");
        }
    }

    /**
     * Displays developer roles.
     */
    private void showRoles(Developer developer) {
        if (CollectionUtils.isNotEmpty(developer.getRoles())) {
            String roles = developer.getRoles().stream()
                    .map(String::trim)
                    .filter(role -> !role.isEmpty())
                    .collect(Collectors.joining(", "));
            getLog().info("Roles:       " + roles);
        } else {
            getLog().info("Roles:       N/A");
        }
    }

    private void showProperties(Developer developer) {
        if (MapUtils.isNotEmpty(developer.getProperties())) {
            String props = developer.getProperties().entrySet().stream()
                    .map(entry -> entry.getKey() + ": " + entry.getValue())
                    .collect(Collectors.joining(", "));
            getLog().info("Properties:   " + props);
        } else {
            getLog().info("Properties:   N/A");
        }
    }
}
