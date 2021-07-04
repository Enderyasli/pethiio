package com.pethiio.android.ui.main.view.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pethiio.android.R;
import com.pethiio.android.data.model.LookUpsResponse;
import com.pethiio.android.data.model.PethiioResponse;
import com.pethiio.android.data.model.filter.PetSearchFilterResponse;
import com.pethiio.android.ui.base.ViewModelFactory;
import com.pethiio.android.ui.main.view.customViews.NoDefaultSpinner;
import com.pethiio.android.ui.main.viewmodel.DashBoardViewModel;
import com.pethiio.android.utils.CommonMethods;
import com.pethiio.android.utils.Constants;
import com.pethiio.android.utils.Resource;
import com.warkiz.tickseekbar.OnSeekChangeListener;
import com.warkiz.tickseekbar.SeekParams;
import com.warkiz.tickseekbar.TickSeekBar;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class FilterBottomSheet extends BottomSheetDialogFragment {

    FilterBottomSheet instance;
    DashBoardViewModel viewModel;

    TextView distanceTitleTv, ageTv, ageValueTv, genderTitle, purposeTitle, breedTitle, filterClearButton, distanceTv;

    RadioGroup purposeRadioGroup, breedRadioGroup;
    NoDefaultSpinner genderSpinner;
    Button filterButton;

    TickSeekBar distanceSeekBar, ageSeekBar;
    List<String> gender, genderKeys, breed, breedKeys, purpose, purposeKeys;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((View) getView().getParent()).setBackgroundColor(Color.TRANSPARENT);


    }


    @Override
    public void onDestroy() {
        //unregister event bus
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private String getLocalizedString(String key, List<PethiioResponse> pethiioResponse) {

        for (int i = 0; i < pethiioResponse.size(); i++) {

            if (pethiioResponse.get(i).getKey().equals(key))
                return pethiioResponse.get(i).getValue();
        }
        return "";
    }

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.filter_bottom_sheet, container, false);
        init(root);

        viewModel =
                ViewModelProviders.of(this, new ViewModelFactory()).get(DashBoardViewModel.class);

        viewModel.fetchSearchFilterListPageData();
        viewModel.fetchFilterList();

        viewModel.getSearchFilterListFields().observe(getViewLifecycleOwner(), new Observer<Resource<List<PethiioResponse>>>() {
            @Override
            public void onChanged(Resource<List<PethiioResponse>> listResource) {

                List<PethiioResponse> pethiioResponse = listResource.getData();
                if (pethiioResponse != null) {

                    distanceTitleTv.setText(getLocalizedString(Constants.petSearchFilterDistanceTitle, pethiioResponse));
                    ageTv.setText(getLocalizedString(Constants.petSearchFilterAgeTitle, pethiioResponse));
                    genderTitle.setText(getLocalizedString(Constants.petSearchFilterGenderTitle, pethiioResponse));
                    purposeTitle.setText(getLocalizedString(Constants.petSearchFilterPurposeTitle, pethiioResponse));
                    breedTitle.setText(getLocalizedString(Constants.petSearchFilterAnimalTitle, pethiioResponse));
                    distanceTitleTv.setText(getLocalizedString(Constants.petSearchFilterDistanceTitle, pethiioResponse));
                    filterButton.setText(getLocalizedString(Constants.petSearchFilterButton, pethiioResponse));
                    filterClearButton.setText(getLocalizedString(Constants.petSearchFilterCleanButton, pethiioResponse));
                    genderSpinner.setPrompt(getLocalizedString(Constants.petSearchFilterGenderTitle, pethiioResponse));

                }


            }
        });

        viewModel.getSearchFilterListLookUps().observe(getViewLifecycleOwner(), listResource -> {

            List<LookUpsResponse> lookUpsResponses = listResource.getData();

            gender = CommonMethods.getLookUps(Constants.lookUpGender, lookUpsResponses);
            genderKeys = CommonMethods.getLookUpKeys(Constants.lookUpGender, lookUpsResponses);
            breed = CommonMethods.getLookUps(Constants.lookUpAnimals, lookUpsResponses);
            breedKeys = CommonMethods.getLookUpKeys(Constants.lookUpAnimals, lookUpsResponses);
            purpose = CommonMethods.getLookUps(Constants.lookUpPurpose, lookUpsResponses);
            purposeKeys = CommonMethods.getLookUpKeys(Constants.lookUpPurpose, lookUpsResponses);

            for (String str : breed) {
                CommonMethods.addRadioButton(str, breedRadioGroup, getContext());
            }
            for (String str : purpose) {
                CommonMethods.addRadioButton(str, purposeRadioGroup, getContext());
            }


            ArrayAdapter genderAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, gender);
            genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            genderSpinner.setAdapter(genderAdapter);


            viewModel.getSearchFilterList().observe(this, petSearchFilterResponseResource -> {

                PetSearchFilterResponse petSearchFilterResponse = petSearchFilterResponseResource.getData();

                if (petSearchFilterResponse != null) {

                    genderSpinner.setSelection(genderKeys.indexOf(petSearchFilterResponse.getGender()));
                    distanceSeekBar.setProgress(petSearchFilterResponse.getMaximumDistance());
                    distanceTv.setText(String.valueOf(petSearchFilterResponse.getMaximumDistance()));

                    ageSeekBar.setProgress(petSearchFilterResponse.getMaximumAge());
                    ageValueTv.setText(String.valueOf(petSearchFilterResponse.getMaximumAge()));

//                    breedRadioGroup.check(breedKeys.indexOf(String.valueOf(petSearchFilterResponse.getBreedId())));
                    purposeRadioGroup.check(0);

                }

            });

        });


        return root;
    }

    private void filterUpdate(){

    }


    public static FilterBottomSheet newInstance() {
        return new FilterBottomSheet();
    }

    @SuppressLint("CutPasteId")
    private void init(View root) {
        distanceSeekBar = root.findViewById(R.id.distance_seek_bar);
        distanceTv = root.findViewById(R.id.distance_km_tv);

        distanceTitleTv = root.findViewById(R.id.distance_tv);
        ageTv = root.findViewById(R.id.age_tv);
        ageValueTv = root.findViewById(R.id.age_value_tv);
        purposeTitle = root.findViewById(R.id.purpose_tv);
        breedTitle = root.findViewById(R.id.breed_tv);
        genderTitle = root.findViewById(R.id.genderLy).findViewById(R.id.title_tv);
        purposeRadioGroup = root.findViewById(R.id.radio_group_purpose);
        breedRadioGroup = root.findViewById(R.id.radio_group_breed);
        genderSpinner = root.findViewById(R.id.genderLy).findViewById(R.id.spinner);
        filterButton = root.findViewById(R.id.filter_button);
        filterClearButton = root.findViewById(R.id.filter_clear_button);
        ageSeekBar = root.findViewById(R.id.age_seek_bar);


        distanceTv.setText(distanceSeekBar.getProgress() + " km");

        distanceSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSeeking(SeekParams seekParams) {
                distanceTv.setText(seekParams.progress + " km");
            }

            @Override
            public void onStartTrackingTouch(TickSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(TickSeekBar seekBar) {
            }
        });


        ageValueTv.setText(String.valueOf(ageSeekBar.getProgress()));

        ageSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSeeking(SeekParams seekParams) {
                ageValueTv.setText(String.valueOf(seekParams.progress));
            }

            @Override
            public void onStartTrackingTouch(TickSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(TickSeekBar seekBar) {
            }
        });


    }

    @Override
    public void setupDialog(@NonNull @NotNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
    }
}
