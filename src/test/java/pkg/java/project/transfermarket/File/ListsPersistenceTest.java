package pkg.java.project.transfermarket.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pkg.java.project.transfermarket.Backend.entities.Owner;
import pkg.java.project.transfermarket.Backend.entities.Team;
import pkg.java.project.transfermarket.Util.Tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ListsPersistenceTest {
    private final Map<String, String> originalFiles = Map.of(
            FileManager.PLAYER_FILE, "",
            FileManager.MANAGER_FILE, "",
            FileManager.TEAM_FILE, "",
            FileManager.OWNER_FILE, "",
            FileManager.ADMIN_FILE, ""
    );

    private java.util.HashMap<String, String> backups;

    @BeforeEach
    void setUp() throws IOException {
        backups = new java.util.HashMap<>();
        for (String filename : originalFiles.keySet()) {
            Path path = Path.of(filename);
            backups.put(filename, Files.exists(path) ? Files.readString(path) : null);
            Files.writeString(path, "");
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        for (Map.Entry<String, String> entry : backups.entrySet()) {
            Path path = Path.of(entry.getKey());
            if (entry.getValue() == null) {
                Files.deleteIfExists(path);
            } else {
                Files.writeString(path, entry.getValue());
            }
        }
    }

    @Test
    void unownedTeamForSaleSurvivesSync() throws IOException {
        Files.writeString(Path.of(FileManager.TEAM_FILE), "1,Free FC,None,0,500.0,None,250.0%n".formatted());

        Lists.syncAllFiles();

        assertEquals(1, Lists.getTeamList().size());
        Team team = Lists.getTeamList().get(0);
        assertEquals("Free FC", team.getTeamName());
        assertNull(team.getOwner());
        assertNull(team.getManager());
        assertEquals(250.0, team.getTeamPrice());
        assertEquals(List.of("1,Free FC,None,0,500.0,None,250.0"), Files.readAllLines(Path.of(FileManager.TEAM_FILE)));
    }

    @Test
    void purchasedTeamRestoresOwnerRelationshipAfterReload() throws IOException {
        Files.writeString(Path.of(FileManager.OWNER_FILE), "1,Buyer,1000.0,pass,None%n".formatted());
        Files.writeString(Path.of(FileManager.TEAM_FILE), "1,Market FC,None,0,300.0,None,200.0%n".formatted());
        Lists.syncAllFiles();

        Owner owner = Lists.getOwnerList().get(0);
        Team team = Lists.getTeamList().get(0);
        owner.setBudget(owner.getBudget() - team.getTeamPrice());
        owner.setTeam(team);
        team.setOwner(owner);
        FileManager.overWriteObjectFile(FileManager.OWNER_FILE, Lists.getOwnerList());
        FileManager.overWriteObjectFile(FileManager.TEAM_FILE, Lists.getTeamList());

        Lists.syncAllFiles();

        Owner reloadedOwner = Lists.getOwnerList().get(0);
        Team reloadedTeam = Lists.getTeamList().get(0);
        assertEquals("Buyer", reloadedTeam.getOwner().getName());
        assertSame(reloadedTeam, reloadedOwner.getTeam());
        assertEquals(800.0, reloadedOwner.getBudget());
    }

    @Test
    void idGeneratorIgnoresBlankLines() throws IOException {
        Files.writeString(Path.of(FileManager.PLAYER_FILE), "%n2,Existing,20,CF,100.0,0,0,true,0%n".formatted());

        assertEquals(1, Tools.idGenerator(FileManager.PLAYER_FILE));
    }
}
