package com.ccvn.flashcard_game.views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.annotation.NonNull;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.ccvn.flashcard_game.Common.CustomDialog;
import com.ccvn.flashcard_game.Common.NetworkChangeReceiver;
import com.ccvn.flashcard_game.databinding.FragmentHomeBinding;
import com.ccvn.flashcard_game.models.Game;
import com.ccvn.flashcard_game.R;
import com.ccvn.flashcard_game.retrofit.APIUtils;
import com.ccvn.flashcard_game.retrofit.GameAPIService;
import com.ccvn.flashcard_game.viewmodels.Adapter.GameAdapter;
import com.ccvn.flashcard_game.viewmodels.ListGameViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


@SuppressWarnings("ALL")
public class ListGameFragment extends Fragment implements GameAdapter.OnGameListener{

    public static final String GAME_ID = "GameID";

    public static final String USERINFO = "user_info";
    public static final String NAME = "Name";
    public static final String AGE = "Age";
    public static final String SEX = "Sex";

    GameAPIService mGameAPIService;
    private CustomDialog mCustomDialog;
    private RecyclerView mGameList;

    private List<Game> mListGame;
    private ListGameViewModel mViewModel;
    String sex;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mListGame= new ArrayList<>();

      // Init GameAPIService class
        mGameAPIService = APIUtils.getAPIService();
      // Init CustomDialog class
        mCustomDialog = new CustomDialog();

       binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
       checkConnection();
        return binding.getRoot();

    }

        private void getListGame(){

            mCustomDialog.showProgressBarDialog(getContext(), "Please Wait...");
            mViewModel = ViewModelProviders.of(this).get(ListGameViewModel.class);

            mViewModel.getAllGame().observe(ListGameFragment.this, new Observer<List<Game>>() {
                @Override
                public void onChanged(List<Game> games) {

            displayData(games);
            mListGame = games;

            mCustomDialog.getProgressBarDialog().dismiss();

                }
            });

        }

     // setup recyclerview and display.
    private void displayData(List<Game> gameList) {

        binding.gameList.setHasFixedSize(true);
        GameAdapter adapter= new GameAdapter(getContext(), gameList, this);
        binding.gameList.setAdapter(adapter);
        runLayoutAnimation(binding.gameList);
        Log.d("XX", String.valueOf(gameList.size()));

    }
    // load animation for item.
    private void runLayoutAnimation(final RecyclerView recyclerView){
        final Context context= recyclerView.getContext();
        final LayoutAnimationController controller=
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_from_right);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();

    }
    // click item on recyclerview.
    @Override
    public void onGameClick(int position) {

          int id = mListGame.get(position).getId();
            readData(id);
    }
    // check conection.
    public void checkConnection(){
        if (NetworkChangeReceiver.isOnline(getContext())){
           getListGame();
        }else {

            Toast.makeText(getActivity(), R.string.check_connection, Toast.LENGTH_SHORT).show();
        }
    }

    // show form to input player's information
    private void showInfoDialog(){

        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.info_form, null);

        Context context;
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(view);
        dialog.setCancelable(false);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final EditText mName = view.findViewById(R.id.edt_name);
        final EditText mAge = view.findViewById(R.id.edt_age);

        final RadioButton mMale = view.findViewById(R.id.male);
        final RadioButton mFemale = view.findViewById(R.id.female);

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.bt_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = mName.getText().toString();
                if (mMale.isChecked()){
                    sex = mMale.getText().toString();
                }
                if (mFemale.isChecked()){
                    sex = mFemale.getText().toString();
                }
                String age = mAge.getText().toString();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(age) && (mMale.isChecked() == true || mFemale.isChecked() == true)){

                    SharedPreferences preferences = getContext().getSharedPreferences(USERINFO, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(NAME, name);
                    editor.putInt(AGE, Integer.parseInt(age));
                    editor.putString(SEX, sex);
                    editor.apply();

                    Toast.makeText(getContext(), "You can play now!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else {
                    Toast.makeText(getContext(), "Please fill information!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(layoutParams);

    }

    // read data of SharedPreferences.
    public void readData(int id){

        SharedPreferences preferences = getContext().getSharedPreferences(USERINFO, Context.MODE_PRIVATE);
        String name = preferences.getString(NAME, "");
        int age = preferences.getInt(AGE, 0);
        String sex = preferences.getString(SEX, "");

        if (name.equals("") && sex.equals("") && age == 0){

            showInfoDialog();

        }else {

            Intent intent = new Intent(getActivity(), GameDetailActivity.class);
            intent.putExtra(GAME_ID, id);

            startActivity(intent);

        }

    }

}