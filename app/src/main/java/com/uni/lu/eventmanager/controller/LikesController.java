package com.uni.lu.eventmanager.controller;

import android.app.Activity;
import android.widget.ImageView;

import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.dao.LikesDAOFirestore;
import com.uni.lu.eventmanager.media.GlideApp;
import com.uni.lu.eventmanager.model.EventModel;
import com.uni.lu.eventmanager.model.LikeModel;

import java.util.Random;

public class LikesController {

	private LikesDAOFirestore likeDao;
	private LikeModel         like;

	public LikesController() {
		this.likeDao = new LikesDAOFirestore();
	}

	public boolean isLiked(String eventDocument){
		this.like =  likeDao.search(eventDocument, FirebaseController.getInstance().getUserId());

		return like != null;
	}

	public void likeEvent(Activity activity, EventModel event, ImageView like) {
		if (this.like == null){
			LikeModel likeModel = new LikeModel(event.getCategory(), event.getTitle(), FirebaseController.getInstance().getUserId());
			likeModel.setDocName("liked-" + new Random().nextInt(1000));

			likeDao.save(likeModel);

			this.like = likeModel;
			changeIconLike(like, activity);
		}else {
			LikeModel likeModel = new LikeModel(event.getCategory(), event.getTitle(), FirebaseController.getInstance().getUserId());
			likeModel.setDocName("liked-" + new Random().nextInt(1000));

			likeDao.delete(likeModel);

			this.like = null;
			changeIconLike(like, activity);
		}

	}

	public void changeIconLike(ImageView likeIcon, Activity activity){
		if (this.like != null){
			GlideApp.with(activity)
					.load(R.drawable.ic_liked)
					.into(likeIcon);
		}else{
			GlideApp.with(activity)
					.load(R.drawable.ic_like)
					.into(likeIcon);
		}
	}
}
