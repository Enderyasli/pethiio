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
import com.mohammedalaa.seekbar.DoubleValueSeekBarView;
import com.mohammedalaa.seekbar.OnDoubleValueSeekBarChangeListener;
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

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Response;


public class FilterBottomSheet extends BottomSheetDialogFragment {

    // TODO: 17.07.2021 yas, km range koy
    DashBoardViewModel viewModel;

    TextView distanceTitleTv, ageTv, ageValueTv, genderTitle, purposeTitle, animalTitle, filterClearButton, distanceTv;

    int maxDistance = 1, minDistance = 0, minAge = 0, maxAge = 1;
    RadioGroup purposeRadioGroup;
    NoDefaultSpinner genderSpinner, typeSpinner;
    Button filterButton;


    DoubleValueSeekBarView distanceSeekBar;
    DoubleValueSeekBarView ageSeekBar;
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

    @SuppressLint({"ResourceType", "SetTextI18n"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.filter_bottom_sheet, container, false);

        viewModel =
                ViewModelProviders.of(this, new ViewModelFactory()).get(DashBoardViewModel.class);

        viewModel.fetchSearchFilterListPageData();
        viewModel.fetchFilterList();

        init(root);


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
                    typeSpinner.setPrompt(getLocalizedString(Constants.petSearchFilterAnimalTitle, pethiioResponse));

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


            for (String str : purpose) {
                CommonMethods.addRadioButton(str, purposeRadioGroup, requireContext());
            }


            ArrayAdapter genderAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, gender);
            genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            genderSpinner.setAdapter(genderAdapter);

            ArrayAdapter typeAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, animal);
            genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            typeSpinner.setAdapter(typeAdapter);


            viewModel.getSearchFilterList().observe(this, petSearchFilterResponseResource -> {

                petSearchFilterResponse = petSearchFilterResponseResource.getData();

                if (petSearchFilterResponse != null) {

                    genderSpinner.setSelection(genderKeys.indexOf(petSearchFilterResponse.getGender()));
                    typeSpinner.setSelection(animalKeys.indexOf(String.valueOf(petSearchFilterResponse.getAnimalId())));

                    distanceSeekBar.setCurrentMaxValue(petSearchFilterResponse.getMaximumDistance());

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            distanceSeekBar.setCurrentMinValue(petSearchFilterResponse.getMinimumDistance());
                        }
                    }, 100);


                    maxDistance = petSearchFilterResponse.getMaximumDistance();
                    minDistance = petSearchFilterResponse.getMinimumDistance();
                    distanceTv.setText(petSearchFilterResponse.getMinimumDistance() + " - " + petSearchFilterResponse.getMaximumDistance() + " km");

                    ageSeekBar.setCurrentMaxValue(petSearchFilterResponse.getMaximumAge());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ageSeekBar.setCurrentMinValue(petSearchFilterResponse.getMinimumAge());
                        }
                    }, 100);
                    minAge = petSearchFilterResponse.getMinimumAge();
                    maxAge = petSearchFilterResponse.getMaximumAge();
                    ageValueTv.setText(petSearchFilterResponse.getMinimumAge() + " - " + petSearchFilterResponse.getMaximumAge());


                    if (purposeKeys.size() > purposeKeys.indexOf(petSearchFilterResponse.getPurpose())) {
                        RadioButton purposeRadioButton = (RadioButton) purposeRadioGroup.getChildAt(purposeKeys.indexOf(petSearchFilterResponse.getPurpose()));
                        if (purposeRadioButton != null)
                            purposeRadioButton.setChecked(true);
                        else {
                            RadioButton purposeRadioButton1 = (RadioButton) purposeRadioGroup.getChildAt(0);
                            if (purposeRadioButton1 != null)
                                purposeRadioButton1.setChecked(true);

                        }
                    }


                }

            });

        });


        return root;
    }

    private void filterUpdate(View view) {
        int purposeSelectedId = purposeRadioGroup.getCheckedRadioButtonId();
        RadioButton purposeRadioButton = (RadioButton) view.findViewById(purposeSelectedId);


        String purpose = CommonMethods.getLookUpKey(Constants.lookUpPurpose, purposeRadioButton.getText().toString(), lookUpsResponses);
        String gender = CommonMethods.getLookUpKey(Constants.lookUpGender, genderSpinner.getSelectedItem().toString(), lookUpsResponses);
        String animal = CommonMethods.getLookUpKey(Constants.lookUpAnimals, typeSpinner.getSelectedItem().toString(), lookUpsResponses);


        if (!TextUtils.isEmpty(purpose) && !TextUtils.isEmpty(animal) && !TextUtils.isEmpty(gender)) {
            int animalId = Integer.parseInt(animal);

            SearchFilterRequest searchFilterRequest = new SearchFilterRequest(
                    animalId,
                    purpose,
                    gender,
                    minAge, maxAge, minDistance, maxDistance
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

    @SuppressLint({"CutPasteId", "SetTextI18n"})
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
        genderSpinner = root.findViewById(R.id.genderLy).findViewById(R.id.spinner);
        typeSpinner = root.findViewById(R.id.typeSpinner);
        filterButton = root.findViewById(R.id.filter_button);
        filterClearButton = root.findViewById(R.id.filter_clear_button);
        ageSeekBar = (DoubleValueSeekBarView) root.findViewById(R.id.age_seek_bar);


        distanceSeekBar.setOnRangeSeekBarViewChangeListener(new OnDoubleValueSeekBarChangeListener() {
            @Override
            public void onValueChanged(@Nullable DoubleValueSeekBarView seekBar, int min, int max, boolean fromUser) {

                distanceTv.setText(min + " - " + max + " km");
                maxDistance = max;
                minDistance = min;
            }

            @Override
            public void onStartTrackingTouch(@Nullable DoubleValueSeekBarView seekBar, int min, int max) {

            }

            @Override
            public void onStopTrackingTouch(@Nullable DoubleValueSeekBarView seekBar, int min, int max) {

            }
        });

        ageSeekBar.setOnRangeSeekBarViewChangeListener(new OnDoubleValueSeekBarChangeListener() {
            @Override
            public void onValueChanged(@Nullable DoubleValueSeekBarView seekBar, int min, int max, boolean fromUser) {

                ageValueTv.setText(min + " - " + max);
                maxAge = max;
                minAge = min;
            }

            @Override
            public void onStartTrackingTouch(@Nullable DoubleValueSeekBarView seekBar, int min, int max) {

            }

            @Override
            public void onStopTrackingTouch(@Nullable DoubleValueSeekBarView seekBar, int min, int max) {

            }
        });

        viewModel.fetchFilterList();


    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull @NotNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
    }
}
