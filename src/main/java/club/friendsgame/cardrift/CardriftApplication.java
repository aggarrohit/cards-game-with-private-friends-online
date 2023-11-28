package club.friendsgame.cardrift;

import club.friendsgame.cardrift.data.Tables;
import club.friendsgame.cardrift.models.Player;
import club.friendsgame.cardrift.models.Table;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class CardriftApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CardriftApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		Map<String,Table> tables =  Tables.tables;
//		Tables.addTable("1234",3);
//		Tables.addPlayerToTable(Player.builder()
//						.id(11)
//						.name("test player11")
//				.build(),"1234");
//		Tables.addPlayerToTable(Player.builder()
//				.id(12)
//				.name("test player12")
//				.build(),"1234");
//		Tables.addPlayerToTable(Player.builder()
//				.id(13)
//				.name("test player13")
//				.build(),"1234");
//		Tables.startGame("1234");
	}
}
