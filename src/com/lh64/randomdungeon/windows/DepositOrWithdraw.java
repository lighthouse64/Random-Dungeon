package com.lh64.randomdungeon.windows;



import com.lh64.noosa.BitmapTextMultiline;

import com.lh64.randomdungeon.Dungeon;
import com.lh64.randomdungeon.PixelDungeon;
import com.lh64.randomdungeon.scenes.GameScene;
import com.lh64.randomdungeon.scenes.PixelScene;
import com.lh64.randomdungeon.ui.RedButton;
import com.lh64.randomdungeon.ui.Window;
import com.lh64.randomdungeon.utils.GLog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;

public class DepositOrWithdraw extends Window {
	public static DepositOrWithdraw instance;
	private static final int WIDTH		= 140;
	private static final int BTN_HEIGHT	= 20;
	private static final float GAP		= 2;
	public static int tempcoins = 0;
	
	
	public DepositOrWithdraw(final Boolean deposit){
		super();
		instance = this;
		BitmapTextMultiline message;
		if(deposit == true){
			message = PixelScene.createMultiline("How much gold do you wish to deposit from your " + Dungeon.gold + " gold?",6);
			
		} else{
			message = PixelScene.createMultiline("How much gold do you wish to withdraw from your " + Dungeon.coins + " stored gold?",6);
		}
		message.maxWidth = WIDTH;
		message.measure();
		add( message );
		
		RedButton btn1g = new RedButton("1 gold"){
			@Override
			protected void onClick() {
				tempcoins = 1;
				if(deposit == true){
					if(tempcoins > Dungeon.gold){
						GLog.n("You can't try to put in more money than you have.");
					} else{
						Dungeon.gold -= tempcoins;
						Dungeon.coins += tempcoins;
					}
				} else{
					if(tempcoins > Dungeon.coins){
						GLog.n("You peer into the coinbox with a sullen look, as you yearn for more money in it.");
					} else{
						Dungeon.gold += tempcoins;
						Dungeon.coins -= tempcoins;
					}
				}
			
				hide();
			}
			
		};
		btn1g.setRect( 0, message.y + message.height() + GAP, WIDTH/2, BTN_HEIGHT );
		add(btn1g);
		
		RedButton btn10g = new RedButton("10 gold"){
			@Override
			protected void onClick() {
				tempcoins = 10;
				if(deposit == true){
					if(tempcoins > Dungeon.gold){
						GLog.n("You can't try to put in more money than you have.");
					} else{
						Dungeon.gold -= tempcoins;
						Dungeon.coins += tempcoins;
					}
				} else{
					if(tempcoins > Dungeon.coins){
						GLog.n("You peer into the coinbox with a sullen look, as you yearn for more money in it.");
					}else{
						Dungeon.gold += tempcoins;
						Dungeon.coins -= tempcoins;
					}
				}
			
				hide();
			}
			
		};
		btn10g.setRect( btn1g.right(), message.y + message.height() + GAP, WIDTH/2, BTN_HEIGHT );
		add(btn10g);
		
		RedButton btn100g = new RedButton("100 gold"){
			@Override
			protected void onClick() {
				tempcoins = 100;
				if(deposit == true){
					if(tempcoins > Dungeon.gold){
						GLog.n("You can't try to put in more money than you have.");
					} else{
						Dungeon.gold -= tempcoins;
						Dungeon.coins += tempcoins;
					}
				} else{
					if(tempcoins > Dungeon.coins){
						GLog.n("You peer into the coinbox with a sullen look, as you yearn for more money in it.");
					}else{
						Dungeon.gold += tempcoins;
						Dungeon.coins -= tempcoins;
					}
				}
			
				hide();
			}
		};
		btn100g.setRect(0, btn1g.bottom(), WIDTH/2, BTN_HEIGHT);
		add(btn100g);
		
		RedButton btn1000g = new RedButton("1000 gold"){
			@Override
			protected void onClick() {
				tempcoins = 1000;
				if(deposit == true){
					if(tempcoins > Dungeon.gold){
						GLog.n("You can't try to put in more money than you have.");
					} else{
						Dungeon.gold -= tempcoins;
						Dungeon.coins += tempcoins;
					}
				} else{
					if(tempcoins > Dungeon.coins){
						GLog.n("You peer into the coinbox with a sullen look, as you yearn for more money in it.");
					}
				}
			
				hide();
			}
		};
		btn1000g.setRect(btn100g.right(), btn10g.bottom(), WIDTH/2, BTN_HEIGHT);
		add(btn1000g);
		
		RedButton btn10000g = new RedButton("10,000 gold"){
			@Override
			protected void onClick() {
				tempcoins = 10000;
				if(deposit == true){
					if(tempcoins > Dungeon.gold){
						GLog.n("You can't try to put in more money than you have.");
					} else{
						Dungeon.gold -= tempcoins;
						Dungeon.coins += tempcoins;
					}
				} else{
					if(tempcoins > Dungeon.coins){
						GLog.n("10,000 gold doesn't magically appear from this box.");
					}else{
						Dungeon.gold += tempcoins;
						Dungeon.coins -= tempcoins;
					}
				}
			
				hide();
			}
		};
		btn10000g.setRect(0, btn1000g.bottom(), WIDTH/2, BTN_HEIGHT);
		add(btn10000g);
		
		
		RedButton btnallg = new RedButton("All your gold"){
			@Override protected void onClick(){
				
				if (deposit == true){
					tempcoins = Dungeon.gold;
					if(tempcoins==0){
						GLog.b("It's kinda pointless to transfer nothing.");
					} else{
						Dungeon.gold -= tempcoins;
						Dungeon.coins += tempcoins;
					}
				} else{
					tempcoins = Dungeon.coins;
					if(tempcoins==0){
						GLog.b("It's kinda pointless to transfer nothing.");
					} else{
						Dungeon.gold += tempcoins;
						Dungeon.coins -= tempcoins;
					}
				}
				hide();
			}
		};
		
		btnallg.setRect(btn10000g.right(), btn1000g.bottom(), WIDTH/2, BTN_HEIGHT);
		add(btnallg);
		
		RedButton btncustomgold = new RedButton("Custom amount"){
			@Override protected void onClick(){
				StoreOrTake(deposit);
				hide();
			}
		};
		btncustomgold.setRect(0, btnallg.bottom(), WIDTH, BTN_HEIGHT);
		add(btncustomgold);
		resize( WIDTH, (int)btncustomgold.bottom() );
	}
public void StoreOrTake(final Boolean deposit){
		
		PixelDungeon.instance.runOnUiThread( new Runnable(){
			@Override
			public void run(){
				final EditText input = new EditText(PixelDungeon.instance);
				AlertDialog.Builder builder = new AlertDialog.Builder(PixelDungeon.instance);
				builder.setTitle("Choose how much money you will deposit");

				// Set up the input
				
				input.setInputType(InputType.TYPE_CLASS_TEXT);
				builder.setView(input);

				// Set up the buttons
				builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() { 
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        try{
				    	tempcoins = Integer.parseInt(input.getText().toString());
				        } catch(NumberFormatException e){
				        	
				        	dialog.cancel();
				        	GameScene.show(new WndMessage("You don't seem to know how to do transactions with anything/nthat isn't an integer..."));
				        }
				        
				        if(deposit == true){
				        	if(tempcoins == 0){
				        		dialog.cancel();
				        		GLog.b("What is the point in trying to put nothing in there anyways?");
				        	}
				        	else if(tempcoins < 0){
				        		dialog.cancel();
				        		GLog.n("You can't put in money that you don't have.");
				        	}
				        	else if(tempcoins > Dungeon.gold){
				        		dialog.cancel();
				        		GLog.n("You can't put in money that you don't have.");
				        	}
				        	else{
				        		Dungeon.gold -= tempcoins;
				        		Dungeon.coins += tempcoins;
				        	}
				        } else{
				        	if(tempcoins == 0){
				        		dialog.cancel();
				        		GLog.b("You took out nothing.");
				        	}
				        	else if(tempcoins < 0){
				        		dialog.cancel();
				        		GLog.n("You can't take out money that you don't have.");
				        	} 
				        	else if(tempcoins > Dungeon.coins){
				        		dialog.cancel();
				        		GLog.n("You can't take out money that you don't have.");
				        	}
				        	else{
				        		Dungeon.coins -= tempcoins;
				        		Dungeon.gold += tempcoins;
				        	}
				        }
				        
				    }
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        dialog.cancel();
				    }
				});

				builder.show();
			}
		});
	}
}
