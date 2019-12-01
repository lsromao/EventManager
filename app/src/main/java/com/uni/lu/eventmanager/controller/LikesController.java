package com.uni.lu.eventmanager.controller;

import android.app.Activity;
import android.widget.ImageView;

import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.dao.LikesDAOFirestore;
import com.uni.lu.eventmanager.media.GlideApp;
import com.uni.lu.eventmanager.model.EventModel;
import com.uni.lu.eventmanager.model.LikeModel;

public class LikesController {

	private LikesDAOFirestore likeDao;
	private LikeModel         like;

	public LikesController() {
		this.likeDao = new LikesDAOFirestore();
	}

	public boolean isLiked(String eventDocument, String userId){
		this.like =  likeDao.search(eventDocument, userId);

		return like != null;
	}

	public void likeEvent(Activity activity, EventModel event, ImageView like) {
		if (this.like == null){
			LikeModel likeModel = new LikeModel(event.getCategory(), event.getTitle(), event.getUserId());
			likeModel.setDocName(
					event.getTitle().replaceAll("\\s+", "") +
					event.getUserId());

			likeDao.save(likeModel);

			this.like = likeModel;
			changeIconLike(like, activity);
		}else {
			LikeModel likeModel = new LikeModel(event.getCategory(), event.getTitle(), event.getUserId());
			likeModel.setDocName(
					event.getTitle().replaceAll("\\s+", "") +
					event.getUserId());

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
