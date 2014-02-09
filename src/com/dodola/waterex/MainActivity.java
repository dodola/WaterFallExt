package com.dodola.waterex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.util.Log;
import android.view.Menu;

import com.dodola.model.DuitangInfo;
import com.dodola.waterex.R;
import com.example.android.bitmapfun.util.Helper;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.origamilabs.library.views.StaggeredGridView;

/** This will not work so great since the heights of the imageViews are
 * calculated on the iamgeLoader callback ruining the offsets. To fix this try
 * to get the (intrinsic) image width and height and set the views height
 * manually. I will look into a fix once I find extra time.
 * 
 * @author Maurycy Wojtowicz */
public class MainActivity extends Activity {
	private ImageFetcher mImageFetcher;
	private StaggeredAdapter mAdapter;
	private ContentTask task = new ContentTask(this, 2);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mImageFetcher = new ImageFetcher(this, 240);
		mImageFetcher.setLoadingImage(R.drawable.empty_photo);
		StaggeredGridView gridView = (StaggeredGridView) this.findViewById(R.id.staggeredGridView1);

		int margin = getResources().getDimensionPixelSize(R.dimen.margin);

		gridView.setFastScrollEnabled(true);

		mAdapter = new StaggeredAdapter(MainActivity.this, mImageFetcher);
		gridView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		AddItemToContainer(1, 1);
		AddItemToContainer(2, 1);
		AddItemToContainer(3, 1);

	}

	private void AddItemToContainer(int pageindex, int type) {
		if (task.getStatus() != Status.RUNNING) {
			String url = "http://www.duitang.com/album/1733789/masn/p/" + pageindex + "/24/";
			Log.d("MainActivity", "current url:" + url);
			ContentTask task = new ContentTask(this, type);
			task.execute(url);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private class ContentTask extends AsyncTask<String, Integer, List<DuitangInfo>> {

		private Context mContext;
		private int mType = 1;

		public ContentTask(Context context, int type) {
			super();
			mContext = context;
			mType = type;
		}

		@Override
		protected List<DuitangInfo> doInBackground(String... params) {
			try {
				return parseNewsJSON(params[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<DuitangInfo> result) {
			if (mType == 1) {

				mAdapter.addItemTop(result);
				mAdapter.notifyDataSetChanged();

			} else if (mType == 2) {
				mAdapter.addItemLast(result);
				mAdapter.notifyDataSetChanged();

			}

		}

		@Override
		protected void onPreExecute() {
		}

		public List<DuitangInfo> parseNewsJSON(String url) throws IOException {
			List<DuitangInfo> duitangs = new ArrayList<DuitangInfo>();
			String json = "";
			if (Helper.checkConnection(mContext)) {
				try {
					json = Helper.getStringFromUrl(url);

				} catch (IOException e) {
					Log.e("IOException is : ", e.toString());
					e.printStackTrace();
					return duitangs;
				}
			}
			Log.d("MainActiivty", "json:" + json);

			try {
				if (null != json) {
					JSONObject newsObject = new JSONObject(json);
					JSONObject jsonObject = newsObject.getJSONObject("data");
					JSONArray blogsJson = jsonObject.getJSONArray("blogs");

					for (int i = 0; i < blogsJson.length(); i++) {
						JSONObject newsInfoLeftObject = blogsJson.getJSONObject(i);
						DuitangInfo newsInfo1 = new DuitangInfo();
						newsInfo1.setAlbid(newsInfoLeftObject.isNull("albid") ? "" : newsInfoLeftObject.getString("albid"));
						newsInfo1.setIsrc(newsInfoLeftObject.isNull("isrc") ? "" : newsInfoLeftObject.getString("isrc"));
						newsInfo1.setMsg(newsInfoLeftObject.isNull("msg") ? "" : newsInfoLeftObject.getString("msg"));
						newsInfo1.setHeight(newsInfoLeftObject.getInt("iht"));
						duitangs.add(newsInfo1);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return duitangs;
		}
	}

}
