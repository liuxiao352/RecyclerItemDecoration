package com.github.recycleritemdecoration;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * @data 2018-09-26
 * @desc
 */

public class ContactsUtils {

    /**
     * 获取全部联系人
     * @param context 上下文
     * @return 全部联系人的Model
     */
    public static ArrayList<ContactInfo> getAllContacts(Context context){
        ArrayList<ContactInfo> list = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            ContactInfo info = new ContactInfo();
            //获取姓名
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            info.setName(name);
            //获取手机号的数量
            int isHas = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
            if (isHas > 0) { //如果大于0
                Cursor c = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);
                while (c.moveToNext()) {
                    String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    info.setPhone(number);
                }
                c.close();
            }
            list.add(info);
        }
        cursor.close();
        return list;
    }

}
