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

	private static final int PHOTO_WITH_DATA = 18; // ��SD���еõ�ͼƬ
	private static final int PHOTO_WITH_CAMERA = 37;// ������Ƭ
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
		// �Զ���Context,�������
		Context dialogContext = new ContextThemeWrapper(context,
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
		intent.setType("image/*"); // ����Pictures����Type�趨Ϊimage
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		System.out.println("111");
		if (resultCode == RESULT_OK) { // ���سɹ�
			switch (requestCode) {
			case PHOTO_WITH_CAMERA: {// ���ջ�ȡͼƬ
				String status = Environment.getExternalStorageState();
				if (status.equals(Environment.MEDIA_MOUNTED)) { // �Ƿ���SD��

					Bitmap bitmap = BitmapFactory.decodeFile(Environment
							.getExternalStorageDirectory() + "/image.jpg");

					String imgName = createPhotoFileName();
					// дһ�����������ļ����浽��Ӧ��������
					savePicture(imgName, bitmap);

					if (bitmap != null) {
						// Ϊ��ֹԭʼͼƬ�������ڴ��������������Сԭͼ��ʾ��Ȼ���ͷ�ԭʼBitmapռ�õ��ڴ�
						Bitmap smallBitmap = ImageTools.zoomBitmap(bitmap,
								bitmap.getWidth() / 5, bitmap.getHeight() / 5);

						imageView.setImageBitmap(smallBitmap);
					}
					Toast.makeText(context, "�ѱ��汾Ӧ�õ�files�ļ�����",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(context, "û��SD��", Toast.LENGTH_LONG).show();
				}
				break;
			}
			case PHOTO_WITH_DATA: {// ��ͼ����ѡ��ͼƬ
				System.out.println("!!!");
				ContentResolver resolver = context.getContentResolver();
				// ��Ƭ��ԭʼ��Դ��ַ
				Uri originalUri = data.getData();
				// System.out.println(originalUri.toString());
				// System.out.println(originalUri.toString());
				// //" content://media/external/images/media/15838 "

				// //��ԭʼ·��ת����ͼƬ��·��
				// String selectedImagePath = uri2filePath(originalUri);
				// System.out.println(selectedImagePath);
				// //" /mnt/sdcard/DCIM/Camera/IMG_20130603_185143.jpg "
				try {
					// ʹ��ContentProviderͨ��URI��ȡԭʼͼƬ
					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
							originalUri);

					String imgName = createPhotoFileName();
					// дһ�����������ļ����浽��Ӧ��������
					savePicture(imgName, photo);
					// Log.i("11", imgName);

					if (photo != null) {
						// Ϊ��ֹԭʼͼƬ�������ڴ��������������Сԭͼ��ʾ��Ȼ���ͷ�ԭʼBitmapռ�õ��ڴ�
						Bitmap smallBitmap = ImageTools.zoomBitmap(photo,
								photo.getWidth() / 5, photo.getHeight() / 5);

						imageView.setImageBitmap(smallBitmap);
					}
					// imageView.setImageURI(originalUri); //�ڽ�������ʾͼƬ
					Toast.makeText(context, "�ѱ��汾Ӧ�õ�files�ļ�����",
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

	/** ����ͼƬ��ͬ���ļ��� **/
	private String createPhotoFileName() {
		String fileName = "";
		Date date = new Date(System.currentTimeMillis()); // ϵͳ��ǰʱ��
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		fileName = dateFormat.format(date) + ".jpg";
		return fileName;
	}

	/** ����ͼƬ����Ӧ���� **/
	private void savePicture(String fileName, Bitmap bitmap) {

		FileOutputStream fos = null;
		try {// ֱ��д�����Ƽ��ɣ�û�лᱻ�Զ�������˽�У�ֻ�б�Ӧ�ò��ܷ��ʣ���������д��ᱻ����
			fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// ��ͼƬд��ָ���ļ�����

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

	private void load_images() {
		fileNames.clear();
		getFiles(context.getFilesDir().getPath(), true);
		for (int i = 0; i < fileNames.size(); ++i) {
			String name = fileNames.get(i);
			Log.i("�ļ���", name);
		}
	}
}
