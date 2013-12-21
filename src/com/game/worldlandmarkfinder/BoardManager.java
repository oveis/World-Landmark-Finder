package com.game.worldlandmarkfinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.widget.ImageView;

class DBLevel1 {
    List<Card> mCards = Arrays.asList(new Card(R.drawable.chihuly_gardens, "Chihuly Gardens"),
            new Card(R.drawable.fremont_troll, "Fremont Troll"), new Card(R.drawable.gas_works_park, "Gas Works Park"),
            new Card(R.drawable.pike_market, "Pike Market"), new Card(R.drawable.seattle_great_wheel, "Seattle Great Wheel"),
            new Card(R.drawable.space_needle, "Space Needle"));
    int mDefaultCardId = R.drawable.landmakrs;
    int mBoardRows = 3;
    int mBoardCols = 4;
}

public class BoardManager {
    
    static final int CARD_SELECT = 0;
    static final int CARDS_MATCH_SUCCESS = 1;
    static final int CARDS_MATCH_FAILURE = 2;
    static final int WRONG_CARD_SELECT = 3;
    static final int ALL_CARDS_MATCHED = 4;
    
    private Board mBoard;
	private int mPrevCardPosition = -1;
	private ImageView mPrevImageView = null;
	
	public Board getBoard() {
	    return mBoard;
	}
	
	public void createBoard() {
	    final DBLevel1 dbLevel1 = new DBLevel1();
	    final List<Card> cards = new ArrayList<Card>();
        for(final Card card : dbLevel1.mCards) {  // add twice to make a pair
            cards.add(card);
            cards.add(card);
        }
        final long seed = System.nanoTime();
        Collections.shuffle(cards, new Random(seed));
        mBoard = new Board(cards, dbLevel1.mDefaultCardId, dbLevel1.mBoardCols, dbLevel1.mBoardRows);
    }
	
	public int clickCard(final ImageView imageView, final int position) {
	    if(!mBoard.isMatchedCard(position)) {
	        if(mPrevCardPosition == -1) {
                imageView.setImageResource(mBoard.getCard(position).getId());
                mPrevCardPosition = position;
                mPrevImageView = imageView;
                return CARD_SELECT;
            } else if(mPrevCardPosition != position){
                imageView.setImageResource(mBoard.getCard(position).getId());
                final int prevCardId = mBoard.getCard(mPrevCardPosition).getId();
                final int curCardId = mBoard.getCard(position).getId();
                
                if(prevCardId == curCardId) {
                    mBoard.setMatchedCards(mPrevCardPosition, position);
                    if(mBoard.isCompleted()) {
                        return ALL_CARDS_MATCHED;
                    }
                    mPrevCardPosition = -1;
                    mPrevImageView = null;
                    return CARDS_MATCH_SUCCESS;
                } else {
                    // re-flip selected cards
                    imageView.setImageResource(mBoard.getDefaultCardId());
                    mPrevImageView.setImageResource(mBoard.getDefaultCardId());
                    mPrevCardPosition = -1;
                    mPrevImageView = null;
                    return CARDS_MATCH_FAILURE;
                }                
    	    }
	    }
	    return WRONG_CARD_SELECT;
	}
	
	public void resetBoard(final Set<Landmark> landmarks, final int width, final int height) {
		
	}
}
