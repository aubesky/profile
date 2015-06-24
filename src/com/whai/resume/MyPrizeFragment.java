package com.whai.resume;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.whai.resume.entity.ImageTools;
import com.whai.resume.adapter.ImageAdapter;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("SimpleDateFormat")
public class MyPrizeFragment extends Fragment {

	private static final int PHOTO_WITH_DATA = 18; // ��SD���еõ�ͼƬ
	private static final int PHOTO_WITH_CAMERA = 37;// ������Ƭ
	private static final int RESULT_OK = -1;
	private static final int ST_CAPACITY = 10000000;
	private Activity activity;
	private ActionBar actionBar;

	private Gallery gallery;
	private ImageView imagedemo;
	private View view;
	private String curImagePath = "";

	public MyPrizeFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		setActionBar();

		view = inflater.inflate(R.layout.my_prize, container, false);

		gallery = (Gallery) view.findViewById(R.id.gallery1);
		imagedemo = (ImageView) view.findViewById(R.id.imagedemo);

		imagedemo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openDelDialog();
			}
		});

		loadGallery();
		initImageView();

		return view;
	}

	private void setActionBar() {
		actionBar.setTitle(R.string.title_section4);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = activity;
		actionBar = activity.getActionBar();
	}

	private void openDelDialog() {
		// �Զ���Context,�������
		Context dialogContext = new ContextThemeWrapper(activity,
				android.R.style.Theme_Light);
		String[] choiceItems = new String[1];
		choiceItems[0] = "ɾ����Ƭ";
		ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
				android.R.layout.simple_list_item_1, choiceItems);
		// �Ի������ڸղŶ���õ���������
		AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
		builder.setTitle("����");
		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							delImage(curImagePath);
							loadGallery();
							initImageView();
							break;
						}
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	private void initImageView() {
		imagedemo.setImageDrawable(view.getResources().getDrawable(
				R.drawable.img1));
	}

	private void delImage(String path) {
		File file = new File(path);
		if (file.exists())
			file.delete();
	}

	private void loadGallery() {

		loadImages();
		final ImageAdapter imageAdapter = new ImageAdapter(activity, fileNames);

		gallery.setAdapter(imageAdapter);
		OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				imagedemo.setImageBitmap(imageAdapter.getBitmap(position));
				curImagePath = imageAdapter.getNowImagePath(position);
			}
		};
		gallery.setOnItemClickListener(onItemClickListener);
	}

	public void openImageSelectDialog() {
		// �Զ���Context,�������
		Context dialogContext = new ContextThemeWrapper(activity,
				android.R.style.Theme_Light);
		String[] choiceItems = new String[2];
		choiceItems[0] = "�������"; // ����
		choiceItems[1] = "�������"; // �������ѡ��
		ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
				android.R.layout.simple_list_item_1, choiceItems);
		// �Ի������ڸղŶ���õ���������
		AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
		builder.setTitle("���ͼƬ");
		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0: // ���
							doTakePhoto();
							break;
						case 1: // ��ͼ�������ѡȡ
							doPickPhotoFromGallery();
							break;
						}
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	/** ������ȡͼƬ **/
	private void doPickPhotoFromGallery() {
		Intent intent = new Intent();
		intent.setType("image/*"); // ����Images����Type�趨Ϊimage
		intent.setAction(Intent.ACTION_GET_CONTENT); // ʹ��Intent.ACTION_GET_CONTENT���Action
		startActivityForResult(intent, PHOTO_WITH_DATA); // ȡ����Ƭ�󷵻ص�������
	}

	/** ���ջ�ȡ��Ƭ **/
	private void doTakePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // ����ϵͳ���

		Uri imageUri = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "image.jpg"));
		// ָ����Ƭ����·����SD������image.jpgΪһ����ʱ�ļ���ÿ�����պ����ͼƬ���ᱻ�滻
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

		// ֱ��ʹ�ã�û����С
		startActivityForResult(intent, PHOTO_WITH_CAMERA); // �û�����˴������ȡ
	}

	private Bitmap compressImage(Bitmap bitmap) {
		int capacity = bitmap.getByteCount();
		double compressRatio = capacity * 1.0 / ST_CAPACITY;
		compressRatio = Math.sqrt(compressRatio);
		Double tmp = bitmap.getHeight() / compressRatio;
		int height = tmp.intValue();
		tmp = bitmap.getWidth() / compressRatio;
		int width = tmp.intValue();
		Bitmap imageCompressed = ImageTools.zoomBitmap(bitmap, width, height);
		return imageCompressed;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) { // ���سɹ�
			switch (requestCode) {
			case PHOTO_WITH_CAMERA: { // ���ջ�ȡͼƬ
				String status = Environment.getExternalStorageState();
				if (status.equals(Environment.MEDIA_MOUNTED)) { // �Ƿ���SD��

					Bitmap bitmap = BitmapFactory.decodeFile(Environment
							.getExternalStorageDirectory() + "/image.jpg");

					// ��ͼƬ����ѹ������
					bitmap = compressImage(bitmap);

					String imageName = createPhotoFileName();
					// дһ�����������ļ����浽��Ӧ��������
					saveImage(imageName, bitmap);
					Toast.makeText(activity, "�ѱ��汾Ӧ�õ�files�ļ�����",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(activity, "û��SD��", Toast.LENGTH_LONG).show();
				}
				break;
			}
			case PHOTO_WITH_DATA: {// ��ͼ����ѡ��ͼƬ
				System.out.println("!!!");
				ContentResolver resolver = activity.getContentResolver();
				// ��Ƭ��ԭʼ��Դ��ַ
				Uri originalUri = data.getData();
				// System.out.println(originalUri.toString());
				// System.out.println(originalUri.toString());
				// "content://media/external/images/media/15838 "

				// ��ԭʼ·��ת����ͼƬ��·��
				// String selectedImagePath = uri2filePath(originalUri);
				// System.out.println(selectedImagePath);
				// "/mnt/sdcard/DCIM/Camera/IMG_20130603_185143.jpg "
				try {
					// ʹ��ContentProviderͨ��URI��ȡԭʼͼƬ
					Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver,
							originalUri);

					// ��ͼƬ����ѹ������
					bitmap = compressImage(bitmap);

					String imageName = createPhotoFileName();
					// дһ�����������ļ����浽��Ӧ��������
					saveImage(imageName, bitmap);
					// imageView.setImageURI(originalUri); //�ڽ�������ʾͼƬ
					Toast.makeText(activity, "�ѱ��汾Ӧ�õ�files�ļ�����",
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
		loadGallery();
		super.onActivityResult(requestCode, resultCode, data);
	}

	/** ����ͼƬ��ͬ���ļ��� **/
	private String createPhotoFileName() {
		String fileName = "";
		Date date = new Date(System.currentTimeMillis()); // ϵͳ��ǰʱ��
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		fileName = dateFormat.format(date) + ".jpg";
		return fileName;
	}

	private int getImageCompressRatio(Bitmap bitmap) {

		int capacity = bitmap.getByteCount();
		int ret = ST_CAPACITY * 100 / capacity;
		if (ret > 100)
			return 100;
		return ret;
	}

	/** ����ͼƬ����Ӧ���� **/
	private void saveImage(String fileName, Bitmap bitmap) {

		FileOutputStream fos = null;
		try {// ֱ��д�����Ƽ��ɣ�û�лᱻ�Զ�������˽�У�ֻ�б�Ӧ�ò��ܷ��ʣ���������д��ᱻ����
			fos = activity.openFileOutput(fileName, Context.MODE_PRIVATE);
			bitmap.compress(Bitmap.CompressFormat.JPEG,
					getImageCompressRatio(bitmap), fos);// ��ͼƬд��ָ���ļ�����

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

	private List<String> fileNames = new ArrayList<String>();

	private void getFiles(String Path, boolean IsIterative) // ����Ŀ¼����չ�����Ƿ�������ļ���
	{
		File[] files = new File(Path).listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isFile()) {
				fileNames.add(f.getPath());

				if (!IsIterative)
					break;
			} else if (f.isDirectory() && f.getPath().indexOf("/.") == -1) // ���Ե��ļ��������ļ�/�ļ��У�
				getFiles(f.getPath(), IsIterative);
		}
	}

	private void loadImages() {
		fileNames.clear();
		getFiles(activity.getFilesDir().getPath(), true);
		for (int i = 0; i < fileNames.size(); ++i) {
			String name = fileNames.get(i);
			Log.i("�ļ���", name);
		}
	}
}
