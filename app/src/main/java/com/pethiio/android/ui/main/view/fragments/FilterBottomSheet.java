package com.pethiio.android.ui.main.view.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pethiio.android.R;
import com.pethiio.android.data.EventBus.FilterEvent;
import com.pethiio.android.data.model.LookUpsResponse;
import com.pethiio.android.data.model.PethiioResponse;
import com.pethiio.android.data.model.filter.PetSearchFilterResponse;
import com.pethiio.android.data.model.filter.SearchFilterRequest;
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

import retrofit2.Response;


public class FilterBottomSheet extends BottomSheetDialogFragment {

    DashBoardViewModel viewModel;

    TextView distanceTitleTv, ageTv, ageValueTv, genderTitle, purposeTitle, animalTitle, filterClearButton, distanceTv;

    int distance = 1, age = 1;
    RadioGroup purposeRadioGroup, animalRadioGroup;
    NoDefaultSpinner genderSpinner;
    Button filterButton;

    TickSeekBar distanceSeekBar, ageSeekBar;
    List<String> gender, genderKeys, animal, animalKeys, purpose, purposeKeys;
    PetSearchFilterResponse petSearchFilterResponse;
    List<LookUpsResponse> lookUpsResponses;


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((View) requireView().getParent()).setBackgroundColor(Color.TRANSPARENT);


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
                    animalTitle.setText(getLocalizedString(Constants.petSearchFilterAnimalTitle, pethiioResponse));
                    distanceTitleTv.setText(getLocalizedString(Constants.petSearchFilterDistanceTitle, pethiioResponse));
                    filterButton.setText(getLocalizedString(Constants.petSearchFilterButton, pethiioResponse));
                    filterClearButton.setText(getLocalizedString(Constants.petSearchFilterCleanButton, pethiioResponse));
                    genderSpinner.setPrompt(getLocalizedString(Constants.petSearchFilterGenderTitle, pethiioResponse));

                }

                filterButton.setOnClickListener(v -> filterUpdate(root));

                filterClearButton.setOnClickListener(v -> viewModel.fetchFilterList());


            }
        });

        viewModel.getSearchFilterListLookUps().observe(getViewLifecycleOwner(), listResource -> {

            lookUpsResponses = listResource.getData();

            gender = CommonMethods.getLookUps(Constants.lookUpGender, lookUpsResponses);
            genderKeys = CommonMethods.getLookUpKeys(Constants.lookUpGender, lookUpsResponses);
            animal = CommonMethods.getLookUps(Constants.lookUpAnimals, lookUpsResponses);
            animalKeys = CommonMethods.getLookUpKeys(Constants.lookUpAnimals, lookUpsResponses);
            purpose = CommonMethods.getLookUps(Constants.lookUpPurpose, lookUpsResponses);
            purposeKeys = CommonMethods.getLookUpKeys(Constants.lookUpPurpose, lookUpsResponses);

            for (String str : animal) {
                CommonMethods.addRadioButton(str, animalRadioGroup, requireContext());
            }
            for (String str : purpose) {
                CommonMethods.addRadioButton(str, purposeRadioGroup, requireContext());
            }


            ArrayAdapter genderAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, gender);
            genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            genderSpinner.setAdapter(genderAdapter);


            viewModel.getSearchFilterList().observe(this, petSearchFilterResponseResource -> {

                petSearchFilterResponse = petSearchFilterResponseResource.getData();

                if (petSearchFilterResponse != null) {

                    genderSpinner.setSelection(genderKeys.indexOf(petSearchFilterResponse.getGender()));
                    distanceSeekBar.setMax(10000);
                    distanceSeekBar.setProgress(petSearchFilterResponse.getMaximumDistance());
                    distanceTv.setText(String.valueOf(petSearchFilterResponse.getMaximumDistance()));

                    ageSeekBar.setMax(20);
                    ageSeekBar.setProgress(petSearchFilterResponse.getMaximumAge());
                    ageValueTv.setText(String.valueOf(petSearchFilterResponse.getMaximumAge()));


                    if (purposeKeys.size() >= purposeKeys.indexOf(petSearchFilterResponse.getPurpose())) {
                        RadioButton purposeRadioButton = (RadioButton) purposeRadioGroup.getChildAt(purposeKeys.indexOf(petSearchFilterResponse.getPurpose()));
                        purposeRadioButton.setChecked(true);
                    }
                    if (animalKeys.size() >= animalKeys.indexOf(petSearchFilterResponse.getAnimalId())) {
                        RadioButton typeRadioButton = (RadioButton) animalRadioGroup.getChildAt(animalKeys.indexOf(String.valueOf(petSearchFilterResponse.getAnimalId())));
                        typeRadioButton.setChecked(true);
                    }


                }

            });

        });


        return root;
    }

    private void filterUpdate(View view) {
        int purposeSelectedId = purposeRadioGroup.getCheckedRadioButtonId();
        RadioButton purposeRadioButton = (RadioButton) view.findViewById(purposeSelectedId);

        int animalSelectedId = animalRadioGroup.getCheckedRadioButtonId();
        RadioButton animalRadioButton = (RadioButton) view.findViewById(animalSelectedId);

        String purpose = CommonMethods.getLookUpKey(Constants.lookUpPurpose, purposeRadioButton.getText().toString(), lookUpsResponses);
        String animal = CommonMethods.getLookUpKey(Constants.lookUpAnimals, animalRadioButton.getText().toString(), lookUpsResponses);
        String gender = CommonMethods.getLookUpKey(Constants.lookUpGender, genderSpinner.getSelectedItem().toString(), lookUpsResponses);


        if (!TextUtils.isEmpty(purpose) && !TextUtils.isEmpty(animal) && !TextUtils.isEmpty(gender)) {
            int animalId = Integer.parseInt(animal);

            SearchFilterRequest searchFilterRequest = new SearchFilterRequest(
                    animalId,
                    purpose,
                    gender,
                    0, age, 0, distance
            );

            viewModel.postSearhFilter(searchFilterRequest);

            viewModel.getPostSearchFilter().observe(getViewLifecycleOwner(), new Observer<Resource<Response<Void>>>() {
                @Override
                public void onChanged(Resource<Response<Void>> responseResource) {
                    EventBus.getDefault().post(new FilterEvent());


                }
            });


        }

        new Handler().postDelayed(FilterBottomSheet.this::dismiss, 300);


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
        animalTitle = root.findViewById(R.id.breed_tv);
        genderTitle = root.findViewById(R.id.genderLy).findViewById(R.id.title_tv);
        purposeRadioGroup = root.findViewById(R.id.radio_group_purpose);
        animalRadioGroup = root.findViewById(R.id.radio_group_breed);
        genderSpinner = root.findViewById(R.id.genderLy).findViewById(R.id.spinner);
        filterButton = root.findViewById(R.id.filter_button);
        filterClearButton = root.findViewById(R.id.filter_clear_button);
        ageSeekBar = root.findViewById(R.id.age_seek_bar);


        distanceTv.setText(distanceSeekBar.getProgress() + " km");
        distance = distanceSeekBar.getProgress();


        distanceSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSeeking(SeekParams seekParams) {
                distanceTv.setText(seekParams.progress + " km");
                distance = seekParams.progress;
            }

            @Override
            public void onStartTrackingTouch(TickSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(TickSeekBar seekBar) {
            }
        });


        ageValueTv.setText(String.valueOf(ageSeekBar.getProgress()));
        age = ageSeekBar.getProgress();


        ageSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSeeking(SeekParams seekParams) {
                ageValueTv.setText(String.valueOf(seekParams.progress));
                age = seekParams.progress;
            }

            @Override
            public void onStartTrackingTouch(TickSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(TickSeekBar seekBar) {
            }
        });


    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull @NotNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
    }
}
