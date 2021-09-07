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
import com.pethiio.android.utils.Status;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Response;


public class FilterBottomSheet extends BottomSheetDialogFragment {

    // TODO: 17.07.2021 yas, km range koy
    DashBoardViewModel viewModel;

    TextView distanceTitleTv, ageTv, ageValueTv, genderTitle, purposeTitle, breedTitle, filterClearButton, distanceTv;

    int maxDistance = 1, minDistance = 0, minAge = 0, maxAge = 1, animalId = -1;
    RadioGroup purposeRadioGroup;
    NoDefaultSpinner genderSpinner, breedSpinner;
    //    SearchableSpinner typeSpinner;
    Button filterButton;


    DoubleValueSeekBarView distanceSeekBar;
    DoubleValueSeekBarView ageSeekBar;
    List<String> gender, genderKeys, breed, breedKeys, purpose, purposeKeys;
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

        animalId = getArguments().getInt("animalId");
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
                    breedTitle.setText(getLocalizedString(Constants.petSearchFilterBreedTitle, pethiioResponse));
                    distanceTitleTv.setText(getLocalizedString(Constants.petSearchFilterDistanceTitle, pethiioResponse));
                    filterButton.setText(getLocalizedString(Constants.petSearchFilterButton, pethiioResponse));
                    filterClearButton.setText(getLocalizedString(Constants.petSearchFilterCleanButton, pethiioResponse));
                    genderSpinner.setPrompt(getLocalizedString(Constants.petSearchFilterGenderTitle, pethiioResponse));
                    breedSpinner.setPrompt(getLocalizedString(Constants.petSearchFilterBreedTitle, pethiioResponse));

                }

                filterButton.setOnClickListener(v -> filterUpdate(root));

                filterClearButton.setOnClickListener(v -> viewModel.fetchFilterList());


            }
        });


        viewModel.getSearchFilterListLookUps().observe(getViewLifecycleOwner(), listResource -> {

            lookUpsResponses = listResource.getData();

            gender = CommonMethods.getLookUps(Constants.lookUpGender, lookUpsResponses);
            genderKeys = CommonMethods.getLookUpKeys(Constants.lookUpGender, lookUpsResponses);
            breedKeys = CommonMethods.getLookUpKeys(Constants.lookUpAnimals, lookUpsResponses);
            purpose = CommonMethods.getLookUps(Constants.lookUpPurpose, lookUpsResponses);
            purposeKeys = CommonMethods.getLookUpKeys(Constants.lookUpPurpose, lookUpsResponses);


            int i = 0;
            for (String str : purpose) {
                CommonMethods.addRadioButtonwithId(i, str, purposeRadioGroup, requireContext());
                i++;
            }


            ArrayAdapter genderAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, gender);
            genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            genderSpinner.setAdapter(genderAdapter);


            viewModel.getSearchFilterList().observe(this, petSearchFilterResponseResource -> {


                if (petSearchFilterResponseResource.getStatus().equals(Status.SUCCESS)) {

                    petSearchFilterResponse = petSearchFilterResponseResource.getData();

                    if (petSearchFilterResponse != null) {


                        genderSpinner.setSelection(genderKeys.indexOf(petSearchFilterResponse.getGender()));

                        viewModel.getSearchFilterBreeds().observe(getViewLifecycleOwner(), breedResponse -> {

                            List<PethiioResponse> pethiioResponse = breedResponse.getData();

                            if (pethiioResponse != null) {

                                breed = CommonMethods.getAnimalBreeds(pethiioResponse);
                                breedKeys = CommonMethods.getBreedKeys(pethiioResponse);

                                ArrayAdapter typeAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, breed);
                                typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                breedSpinner.setAdapter(typeAdapter);


                                if (petSearchFilterResponse != null)
                                    breedSpinner.setSelection(breedKeys.indexOf(String.valueOf(petSearchFilterResponse.getBreedId())));

                            }

                        });

                        distanceSeekBar.setCurrentMaxValue(petSearchFilterResponse.getMaximumDistance());

//                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
                        distanceSeekBar.setCurrentMinValue(petSearchFilterResponse.getMinimumDistance());
//                        }
//                    }, 200);

//                    new Handler().postDelayed(() -> distanceSeekBar.setCurrentMinValue(petSearchFilterResponse.getMinimumDistance()), 100);


                        maxDistance = petSearchFilterResponse.getMaximumDistance();
                        minDistance = petSearchFilterResponse.getMinimumDistance();
                        distanceTv.setText(petSearchFilterResponse.getMinimumDistance() + " - " + petSearchFilterResponse.getMaximumDistance() + " km");

                        ageSeekBar.setCurrentMaxValue(petSearchFilterResponse.getMaximumAge());

//                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
                        ageSeekBar.setCurrentMinValue(petSearchFilterResponse.getMinimumAge());
//                        }
//                    }, 200);
//                    new Handler().postDelayed(() -> ageSeekBar.setCurrentMinValue(petSearchFilterResponse.getMinimumAge()), 100);
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


//                    new Handler(Looper.getMainLooper()).post(new Runnable() {
//                        @Override
//                        public void run() {
                        if (animalId > -1)
                            viewModel.fetchFilterBreeds(animalId);
//                        }
//                    });


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
        int animal = Integer.parseInt(breedKeys.get(breedSpinner.getSelectedItemPosition()));


        if (!TextUtils.isEmpty(purpose) && animalId != -1 && animal != -1 && !TextUtils.isEmpty(gender)) {
            Integer breedId = animal;

            if (breedId == 0)
                breedId = null;

            SearchFilterRequest searchFilterRequest = new SearchFilterRequest(
                    animalId,
                    breedId,
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
        genderTitle = root.findViewById(R.id.breed_tv);
        breedTitle = root.findViewById(R.id.breedSpinner).findViewById(R.id.title_tv);
        purposeRadioGroup = root.findViewById(R.id.radio_group_purpose);
        breedSpinner = root.findViewById(R.id.breedSpinner).findViewById(R.id.spinner);
        genderSpinner = root.findViewById(R.id.genderSpinner);
        filterButton = root.findViewById(R.id.filter_button);
        filterClearButton = root.findViewById(R.id.filter_clear_button);
        ageSeekBar = (DoubleValueSeekBarView) root.findViewById(R.id.age_seek_bar);

//        typeSpinner.setTitle("Cins Seç");
//        typeSpinner.setPositiveButton("kapat");


        purposeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                breedSpinner.setEnabled(checkedId != 1);
//                genderSpinner.setEnabled(checkedId != 1);
                genderSpinner.setVisibility(checkedId != 1 ? View.VISIBLE : View.GONE);
                genderTitle.setVisibility(checkedId != 1 ? View.VISIBLE : View.GONE);

            }
        });

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
