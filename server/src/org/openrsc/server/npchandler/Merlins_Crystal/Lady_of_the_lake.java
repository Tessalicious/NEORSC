/**
* Generated By NPCScript :: A scripting engine created for openrsc by Zilent
*/
package org.openrsc.server.npchandler.Merlins_Crystal;
import org.openrsc.server.Config;
import org.openrsc.server.event.DelayedQuestChat;
import org.openrsc.server.event.SingleEvent;
import org.openrsc.server.model.ChatMessage;
import org.openrsc.server.model.MenuHandler;
import org.openrsc.server.model.Npc;
import org.openrsc.server.model.Player;
import org.openrsc.server.model.Quest;
import org.openrsc.server.model.Quests;
import org.openrsc.server.model.World;
import org.openrsc.server.npchandler.NpcHandler;



public class Lady_of_the_lake implements NpcHandler {

	public void handleNpc(final Npc npc, final Player owner) throws Exception {
		npc.blockedBy(owner);
		owner.setBusy(true);
		Quest q = owner.getQuest(Quests.MERLINS_CRYSTAL);
		if(q != null) {
			if(q.finished()) {
				noQuestStarted(npc, owner);
			} else {
				switch(q.getStage()) {
					case 5:
						questStage5(npc, owner);
						break;
					case 6:
						questStage6(npc, owner);
						break;
					default:
						noQuestStarted(npc, owner);
				}
			}
		} else {
			noQuestStarted(npc, owner);
		}
	}
	

	private void noQuestStarted(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"Hello old lady"}, true) {
			public void finished() {
				World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Greetings young one"}) {
					public void finished() {
						owner.setBusy(false);
						npc.unblock();
					}
				});
			}
		});
	}
	
	
	private void questStage5(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Hello young one, how can I help?"}, true) {
			public void finished() {
				World.getDelayedEventHandler().add(new SingleEvent(owner, 1500) {
					public void action() {
						final String[] options107 = {"I'm in search of Excalibur ", "Goodbye"};
						owner.setBusy(false);
						owner.sendMenu(options107);
						owner.setMenuHandler(new MenuHandler(options107) {
							public void handleReply(final int option, final String reply) {
								owner.setBusy(true);
								for(Player informee : owner.getViewArea().getPlayersInView()) {
									informee.informOfChatMessage(new ChatMessage(owner, reply, npc));
								}
								switch(option) {
									case 0:
										quest(npc, owner);
										break;
									case 1:
										goodBye(npc, owner);
										break;
								}
							}
						});
					}
				});
			}
		});
	}
		
	
	private void questStage6(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"What are you doing here young one?", "Meet me at the port sarim jewellery shop"}, true) {
			public void finished() {
				owner.setBusy(false);
				npc.unblock();	
			}
		});
	}
		
		
	private void quest(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(npc, owner, new String[] {"Yes I do have the sword", "But if you want it", "Meet me at port sarim at the jewellery shop"}) {
			public void finished() {
				owner.incQuestCompletionStage(Quests.MERLINS_CRYSTAL);
				owner.setBusy(false);
				npc.unblock();	
			}
		});
	}
	
	
	private void goodBye(final Npc npc, final Player owner) {
		World.getDelayedEventHandler().add(new DelayedQuestChat(owner, npc, new String[] {"goodbye"}) {
			public void finished() {
				owner.setBusy(false);
				npc.unblock();	
			}
		});
	}
	
}