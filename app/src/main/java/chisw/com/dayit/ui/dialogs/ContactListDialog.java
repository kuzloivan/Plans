package chisw.com.dayit.ui.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import chisw.com.dayit.R;

/**
 * Created by alex on 07.07.15.
 */
public class ContactListDialog extends DialogFragment implements AdapterView.OnItemClickListener {
    ListView mContactsLV;
    String[] mListItems;

    @Override
    public View onCreateView(LayoutInflater pInflater, ViewGroup pContainer, Bundle pSavedInstanceState){
        View view = pInflater.inflate(R.layout.dialog_contact_list, null, false);
        mContactsLV = (ListView) view.findViewById(R.id.contacts_lv);
        ArrayList<String> contactsArrayList = getArguments().getStringArrayList("contactsArrayList");
        mListItems = new String[contactsArrayList.size()];
        mListItems = contactsArrayList.toArray(mListItems);
        getDialog().getWindow().setTitle("Contacts");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle pSavedInstanceState){
        super.onActivityCreated(pSavedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, mListItems);

        mContactsLV.setAdapter(adapter);
        mContactsLV.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> pParent,View pView, int pPosition, long pId){
        dismiss();
        Toast.makeText(getActivity(), mListItems[pPosition],Toast.LENGTH_SHORT).show();
    }
}
