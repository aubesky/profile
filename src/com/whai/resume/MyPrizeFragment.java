package com.whai.resume;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.whai.resume.entity.ImageTools;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

public class MyPrizeFragment extends Fragment {

	private static final int PHOTO_WITH_DATA = 18; // 从SD卡中得到图片
	private static final int PHOTO_WITH_CAMERA = 37;// 拍摄照片
	private static final int RESULT_OK = -1;

	ImageView imageView;
	Context context;

	public MyPrizeFragment() {
		// TODO Auto-generated constructor stub
	}

	public MyPrizeFragment(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.my_prize, container, false);
		Button button = (Button) view.findViewById(R.id.select_pic);
		imageView = (ImageView) view.findViewById(R.id.imageView);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openPictureSelectDialog(context);
			}
		});
		return view;
	}

	private void openPictureSelectDialog(Context context) {
		// 自定义Context,添加主题
		Context dialogContext = new ContextThemeWrapper(context,
				android.R.style.Theme_Light);
		String[] choiceItems = new String[2];
		choiceItems[0] = "相机拍摄"; // 拍照
		choiceItems[1] = "本地相册"; // 从相册中选择
		ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
				android.R.layout.simple_list_item_1, choiceItems);
		// 对话框建立在刚才定义好的上下文上
		AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
		builder.setTitle("添加图片");
		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0: // 相机
							doTakePhoto();
							break;
						case 1: // 从图库相册中选取
							doPickPhotoFromGallery();
							break;
						}
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	/** 从相册获取图片 **/
	private void doPickPhotoFromGallery() {
		Intent intent = new Intent();
		intent.setType("image/*"); // 开启Pictures画面Type设定为image
		intent.setAction(Intent.ACTION_GET_CONTENT); // 使用Intent.ACTION_GET_CONTENT这个Action
		startActivityForResult(intent, PHOTO_WITH_DATA); // 取得相片后返回到本画面
	}

	/** 拍照获取相片 **/
	private void doTakePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用系统相机

		Uri imageUri = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "image.jpg"));
		// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

		// 直接使用，没有缩小
		startActivityForResult(intent, PHOTO_WITH_CAMERA); // 用户点击了从相机获取
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		System.out.println("111");
		if (resultCode == RESULT_OK) { // 返回成功
			switch (requestCode) {
			case PHOTO_WITH_CAMERA: {// 拍照获取图片
				String status = Environment.getExternalStorageState();
				if (status.equals(Environment.MEDIA_MOUNTED)) { // 是否有SD卡

					Bitmap bitmap = BitmapFactory.decodeFile(Environment
							.getExternalStorageDirectory() + "/image.jpg");

					String imgName = createPhotoFileName();
					// 写一个方法将此文件保存到本应用下面啦
					savePicture(imgName, bitmap);

					if (bitmap != null) {
						// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
						Bitmap smallBitmap = ImageTools.zoomBitmap(bitmap,
								bitmap.getWidth() / 5, bitmap.getHeight() / 5);

						imageView.setImageBitmap(smallBitmap);
					}
					Toast.makeText(context, "已保存本应用的files文件夹下",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(context, "没有SD卡", Toast.LENGTH_LONG).show();
				}
				break;
			}
			case PHOTO_WITH_DATA: {// 从图库中选择图片
				System.out.println("!!!");
				ContentResolver resolver = context.getContentResolver();
				// 照片的原始资源地址
				Uri originalUri = data.getData();
				// System.out.println(originalUri.toString());
				// System.out.println(originalUri.toString());
				// //" content://media/external/images/media/15838 "

				// //将原始路径转换成图片的路径
				// String selectedImagePath = uri2filePath(originalUri);
				// System.out.println(selectedImagePath);
				// //" /mnt/sdcard/DCIM/Camera/IMG_20130603_185143.jpg "
				try {
					// 使用ContentProvider通过URI获取原始图片
					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
							originalUri);

					String imgName = createPhotoFileName();
					// 写一个方法将此文件保存到本应用下面啦
					savePicture(imgName, photo);
					// Log.i("11", imgName);

					if (photo != null) {
						// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
						Bitmap smallBitmap = ImageTools.zoomBitmap(photo,
								photo.getWidth() / 5, photo.getHeight() / 5);

						imageView.setImageBitmap(smallBitmap);
					}
					// imageView.setImageURI(originalUri); //在界面上显示图片
					Toast.makeText(context, "已保存本应用的files文件夹下",
							Toast.LENGTH_LONG).show();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			}
		}
		load_images();
		super.onActivityResult(requestCode, resultCode, data);
	}

	/** 创建图片不同的文件名 **/
	private String createPhotoFileName() {
		String fileName = "";
		Date date = new Date(System.currentTimeMillis()); // 系统当前时间
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		fileName = dateFormat.format(date) + ".jpg";
		return fileName;
	}

	/** 保存图片到本应用下 **/
	private void savePicture(String fileName, Bitmap bitmap) {

		FileOutputStream fos = null;
		try {// 直接写入名称即可，没有会被自动创建；私有：只有本应用才能访问，重新内容写入会被覆盖
			fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 把图片写入指定文件夹中

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fos) {
					fos.close();
					fos = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void readPicture(String fileName) {
		try {
			FileInputStream inputStream = context.openFileInput(fileName);
			byte[] bytes = new byte[1024 * 1024];
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			while (inputStream.read(bytes) != -1) {
				arrayOutputStream.write(bytes, 0, bytes.length);
			}
			inputStream.close();
			arrayOutputStream.close();
			Bitmap smallBitmap = BitmapFactory.decodeByteArray(bytes, 0,
					bytes.length);
			imageView.setImageBitmap(smallBitmap);
			// String content = new String(arrayOutputStream.toByteArray());
			// showTextView.setText(content);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<String> fileNames = new ArrayList<String>();

	private void getFiles(String Path, boolean IsIterative) // 搜索目录，扩展名，是否进入子文件夹
	{
		File[] files = new File(Path).listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isFile()) {
				fileNames.add(f.getPath());

				if (!IsIterative)
					break;
			} else if (f.isDirectory() && f.getPath().indexOf("/.") == -1) // 忽略点文件（隐藏文件/文件夹）
				getFiles(f.getPath(), IsIterative);
		}
	}

	private void load_images() {
		fileNames.clear();
		getFiles(context.getFilesDir().getPath(), true);
		for (int i = 0; i < fileNames.size(); ++i) {
			String name = fileNames.get(i);
			Log.i("文件名", name);
		}
	}
}
