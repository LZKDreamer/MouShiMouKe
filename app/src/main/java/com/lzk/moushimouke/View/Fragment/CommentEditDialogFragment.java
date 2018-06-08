package com.lzk.moushimouke.View.Fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzk.moushimouke.Model.Bean.MyUser;
import com.lzk.moushimouke.Model.Utils.IsNetWorkConnectedUtils;
import com.lzk.moushimouke.MyApplication;
import com.lzk.moushimouke.Presenter.CommentPresenter;
import com.lzk.moushimouke.R;
import com.lzk.moushimouke.View.Interface.ICommentActivityDataCallBack;
import com.lzk.moushimouke.View.Interface.ICommentSendResultCallBack;

import cn.bmob.v3.BmobUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentEditDialogFragment extends DialogFragment implements View.OnClickListener,ICommentSendResultCallBack{

    EditText mCommentEditContent;
    ImageView mCommentEditSend;
    TextView mCommentEditNum;
    private Dialog mDialog;
    private InputMethodManager mInputMethodManager;
    private ICommentActivityDataCallBack mCallBack;
    private IsNetWorkConnectedUtils mConnectedUtils;
    private CommentPresenter mCommentPresenter;
    public static CommentEditDialogFragment sCommentEditDialogFragment;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog=new Dialog(getActivity(),R.style.BottomCommentDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.fragment_comment_edit_dialog);
        mDialog.setCancelable(true);
        Window window=mDialog.getWindow();
        WindowManager.LayoutParams layoutParams=window.getAttributes();
        layoutParams.gravity=Gravity.BOTTOM;
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        mCommentEditContent=mDialog.findViewById(R.id.comment_edit_content);
        mCommentEditSend=mDialog.findViewById(R.id.comment_edit_send);
        mCommentEditNum=mDialog.findViewById(R.id.comment_edit_num);
        setCommentText();
        setSoftKeyboard();
        mCommentEditContent.addTextChangedListener(mTextWatcher);
        mCommentEditSend.setOnClickListener(this);
        mCommentPresenter=new CommentPresenter(this);
        sCommentEditDialogFragment=this;
        return mDialog;
    }

    public static CommentEditDialogFragment newInstance(String postId,MyUser postUser){
        Bundle args=new Bundle();
        args.putString("postId",postId);
        args.putSerializable("postUser",postUser);

        CommentEditDialogFragment dialogFragment=new CommentEditDialogFragment();
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    private TextWatcher mTextWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
           int length=mCommentEditContent.getText().length();
           mCommentEditNum.setText(String.valueOf(140-length));
           if (length==0){
               mCommentEditSend.setEnabled(false);
               mCommentEditSend.setImageResource(R.drawable.ic_comment_commit_nor);
           }else {
               mCommentEditSend.setEnabled(true);
               mCommentEditSend.setImageResource(R.drawable.ic_comment_commit_ok);
           }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void setCommentText() {
        mCallBack = (ICommentActivityDataCallBack) getActivity();
        String content = mCallBack.getActivityComment();
        if (content.length() != 0) {
            mCommentEditContent.setText(content);
            mCommentEditSend.setEnabled(true);
            mCommentEditSend.setImageResource(R.drawable.ic_comment_commit_ok);
        }else {
            mCommentEditSend.setEnabled(false);
            mCommentEditSend.setImageResource(R.drawable.ic_comment_commit_nor);
        }
    }

    private void setSoftKeyboard(){
        mCommentEditContent.setFocusable(true);
        mCommentEditContent.setFocusableInTouchMode(true);
        mCommentEditContent.requestFocus();
        /*为 commentEditText 设置监听器，在 DialogFragment 绘制完后立即呼出软键盘，呼出成功后即注销*/
        mCommentEditContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
               mInputMethodManager= (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
               if (mInputMethodManager.showSoftInput(mCommentEditContent,0)){
                   mCommentEditContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
               }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.comment_edit_send:
                String content=mCommentEditContent.getText().toString();
                String id=getArguments().getString("postId");
                MyUser user= BmobUser.getCurrentUser(MyUser.class);
                mConnectedUtils=new IsNetWorkConnectedUtils();
                boolean state=mConnectedUtils.IsNetWorkConnected(getActivity());
                if (state){
                    mCommentPresenter.requestSendComment(content,id,user, (MyUser) getArguments().getSerializable("postUser"));
                }else {
                    createSnackBar(mCommentEditContent,getResources().getString(R.string.network_error));
                }
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mCallBack.getDialogFragmentComment(mCommentEditContent.getText().toString());
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        mCallBack.getDialogFragmentComment(mCommentEditContent.getText().toString());
        super.onCancel(dialog);
    }


    @Override
    public void getCommentSendResult(boolean result) {
        if (result){
            mCommentEditContent.setText("");
            dismiss();
            Toast.makeText(MyApplication.getContext(),R.string.comment_success,Toast.LENGTH_SHORT).show();
        }else {
            mCommentEditContent.setText("");
            dismiss();
            Toast.makeText(MyApplication.getContext(),R.string.comment_failed,Toast.LENGTH_SHORT).show();
        }
    }

    private void createSnackBar(View view,String prompt){
        Snackbar.make(view,prompt,Snackbar.LENGTH_SHORT).show();
    }
}
