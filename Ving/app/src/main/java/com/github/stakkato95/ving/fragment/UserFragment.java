package com.github.stakkato95.ving.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.stakkato95.imageloader.ImageLoader;
import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.activity.MainActivity;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.bo.Photo;
import com.github.stakkato95.ving.bo.User;
import com.github.stakkato95.ving.loader.DataLoader;
import com.github.stakkato95.ving.processor.PhotosProcessor;
import com.github.stakkato95.ving.processor.Processor;
import com.github.stakkato95.ving.processor.UserProcessor;
import com.github.stakkato95.ving.source.VkDataSource;
import com.github.stakkato95.ving.utils.MetricsUtils;
import com.github.stakkato95.ving.utils.ProcessingUtils;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * Created by Artyom on 04.02.2015.
 */
public class UserFragment extends Fragment implements DataLoader.Callback<User[]> {

    private TextView mErrorText;
    private ProgressBar mProgressBar;

    private ImageView mBackground;
    private ImageView mUserImage;
    private ImageView mOnlineImage;
    private TextView mUserName;
    private TextView mUserState;
    private TextView mUserStatus;

    private ScrollView mScrollContainer;
    private HorizontalScrollView mCountersScrollContainer;
    private LinearLayout mCountersLinearContainer;
    private LinearLayout mPhotosLinearContainer;
    private LinearLayout mRelativesLinearContainer;
    private LinearLayout mSchoolsLinearContainer;
    private LinearLayout mUniversitiesLinearContainer;

    private TextView mHeadGeneral;
    private TextView mHeadRelatives;
    private TextView mHeadContacts;
    private TextView mHeadSchools;
    private TextView mHeadUniversities;
    private TextView mHeadBeliefs;
    private TextView mHeadPersonal;
    private TextView mHeadAbout;

    private TextView mBirthday;
    private TextView mHomeTown;
    private TextView mRelation;
    private TextView mLangs;
    private TextView mSkype;
    private TextView mSite;
    private TextView mCity;
    private TextView mCountry;
    private TextView mMobilePhone;
    private TextView mHomePhone;
    private TextView mPolitical;
    private TextView mReligion;
    private TextView mLifeMain;
    private TextView mPeopleMain;
    private TextView mSmoking;
    private TextView mAlcohol;
    private TextView mInspiration;
    private TextView mActivity;
    private TextView mInterests;
    private TextView mMusic;
    private TextView mMovies;
    private TextView mTv;
    private TextView mBooks;
    private TextView mGames;
    private TextView mQuotes;
    private TextView mAbout;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private DataLoader mDataLoader;
    private Processor mProcessor;
    private VkDataSource mVkDataSource;
    private ImageLoader mImageLoader;
    private String mRequestUrl;
    private String mPhotosRequestUrl;

    public static UserFragment newInstance(String userId) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_REQUEST_FIELD, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataLoader = new DataLoader();
        mVkDataSource = VkDataSource.get(getActivity());
        mProcessor = new UserProcessor();
        mImageLoader = ImageLoader.get(getActivity());
        String userId = getArguments().getString(MainActivity.KEY_REQUEST_FIELD);
        //todo builder
        mRequestUrl = Api.getUser() + userId + "&" + Api.FIELD_USER_FIELDS + Api._BIRTHDAY + "," + Api._CONTACTS + "," +
                Api._ONLINE + "," + Api._PHOTO_200 + "," + Api._PHOTO_MAX + "," + Api._SEX + "," + Api._SITE + "," + Api._SKYPE + "," +
                Api._STATUS + "," + Api._LAST_SEEN + "," + Api._COUNTERS + "," + Api._CITY + "," + Api._HOME_TOWN + "," + Api._COUNTRY + "," +
                Api._RELATIVES + "," + Api._PERSONAL + "," + Api._RELATION + "," + Api._SCHOOLS + "," + Api._UNIVERSITIES + "," + Api._ACTIVITIES + "," +
                Api._INTERESTS + "," + Api._MUSIC+ "," + Api._MOVIES + "," + Api._TV+ "," + Api._BOOKS+ "," + Api._GAMES+ "," +
                Api._UNIVERSITIES + "," + Api._ABOUT;

        mPhotosRequestUrl = Api.getPhotos() + userId + "&" + Api.FIELD_ALBUM_ID + Api._ALBUM_PROFILE + "&" + Api.FIELD_PHOTOS_SORT_ORDER + Api.PHOTOS_SORT_ANTICHRONOLOGICAL + "&" + Api.FIELD_PHOTO_SIZES + Api.PHOTOS_SPECIAL_SIZES;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        mProgressBar = (ProgressBar) view.findViewById(android.R.id.progress);
        mErrorText = (TextView) view.findViewById(R.id.loading_error_text_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetFields();
                loadData();
            }
        });

        mBackground = (ImageView) view.findViewById(R.id.background_image);
        mUserImage = (ImageView) view.findViewById(R.id.user_image);
        mOnlineImage = (ImageView) view.findViewById(R.id.online_image);

        mBirthday = (TextView) view.findViewById(R.id.info_general_birthday);
        mHomeTown = (TextView) view.findViewById(R.id.info_general_hometown);
        mRelation = (TextView) view.findViewById(R.id.info_general_relation);
        mLangs = (TextView) view.findViewById(R.id.info_general_langs);
        mSkype = (TextView) view.findViewById(R.id.info_contacts_skype);
        mSite = (TextView) view.findViewById(R.id.info_contacts_site);
        mCity = (TextView) view.findViewById(R.id.info_contacts_city);
        mCountry = (TextView) view.findViewById(R.id.info_contacts_country);
        mMobilePhone = (TextView) view.findViewById(R.id.info_contacts_mobile_phone);
        mHomePhone = (TextView) view.findViewById(R.id.info_contacts_home_phone);
        mPolitical = (TextView) view.findViewById(R.id.info_beliefs_political);
        mReligion = (TextView) view.findViewById(R.id.info_beliefs_religion);
        mLifeMain = (TextView) view.findViewById(R.id.info_beliefs_life_main);
        mPeopleMain = (TextView) view.findViewById(R.id.info_beliefs_people_main);
        mSmoking = (TextView) view.findViewById(R.id.info_beliefs_smoking);
        mAlcohol = (TextView) view.findViewById(R.id.info_beliefs_alcohol);
        mInspiration = (TextView) view.findViewById(R.id.info_beliefs_inspiration);
        mActivity = (TextView) view.findViewById(R.id.info_personal_activity);
        mInterests = (TextView) view.findViewById(R.id.info_personal_interests);
        mMusic = (TextView) view.findViewById(R.id.info_personal_music);
        mMovies = (TextView) view.findViewById(R.id.info_personal_movies);
        mTv = (TextView) view.findViewById(R.id.info_personal_tv);
        mBooks = (TextView) view.findViewById(R.id.info_personal_books);
        mGames = (TextView) view.findViewById(R.id.info_personal_games);
        mQuotes = (TextView) view.findViewById(R.id.info_personal_quotes);
        mAbout= (TextView) view.findViewById(R.id.info_about_field);

        mScrollContainer = (ScrollView)view.findViewById(R.id.scroll_container);
        mCountersScrollContainer = (HorizontalScrollView) view.findViewById(R.id.counters_scroll_container);
        mCountersLinearContainer = (LinearLayout) view.findViewById(R.id.counters_linear_container);
        mPhotosLinearContainer = (LinearLayout) view.findViewById(R.id.photos_linear_container);
        mRelativesLinearContainer = (LinearLayout) view.findViewById(R.id.relatives_linear_container);
        mSchoolsLinearContainer = (LinearLayout) view.findViewById(R.id.schools_linear_container);
        mUniversitiesLinearContainer = (LinearLayout) view.findViewById(R.id.universities_linear_container);

        mHeadGeneral = (TextView) view.findViewById(R.id.info_general);
        mHeadRelatives = (TextView) view.findViewById(R.id.info_relatives);
        mHeadContacts = (TextView) view.findViewById(R.id.info_contacts);
        mHeadSchools = (TextView) view.findViewById(R.id.info_schools);
        mHeadSchools = (TextView) view.findViewById(R.id.info_schools);
        mHeadUniversities = (TextView) view.findViewById(R.id.info_universities);
        mHeadBeliefs = (TextView) view.findViewById(R.id.info_beliefs);
        mHeadPersonal = (TextView) view.findViewById(R.id.info_personal);
        mHeadAbout = (TextView) view.findViewById(R.id.info_about);

        mUserName = (TextView) view.findViewById(R.id.user_name);
        mUserState = (TextView) view.findViewById(R.id.user_state);
        mUserStatus = (TextView) view.findViewById(R.id.user_status);

        loadData();
        return view;
    }

    private void loadData() {
        mDataLoader.getDataAsynch(UserFragment.this, mRequestUrl, mVkDataSource, mProcessor);
        mDataLoader.getDataAsynch(new DataLoader.Callback<Photo[]>() {

            @Override
            public void onLoadingStarted() {

            }

            @Override
            public void onLoadingFinished(Photo[] photos) {
                setPhotos(photos);
                mPhotosLinearContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingError(Exception e) {
                onError(e);
            }

        }, mPhotosRequestUrl, mVkDataSource, new PhotosProcessor());
    }

    private void resetFields() {
        mCountersLinearContainer.removeAllViews();
        mPhotosLinearContainer.removeAllViews();
        mRelativesLinearContainer.removeAllViews();
        mSchoolsLinearContainer.removeAllViews();
        mUniversitiesLinearContainer.removeAllViews();

        mBirthday.setText(getResources().getString(R.string.info_general_birthday));
        mHomeTown.setText(getResources().getString(R.string.info_general_hometown));
        mRelation.setText(getResources().getString(R.string.info_general_relation));
        mLangs.setText(getResources().getString(R.string.info_general_langs));
        mSkype.setText(getResources().getString(R.string.info_contacts_skype));
        mSite.setText(getResources().getString(R.string.info_contacts_site));
        mCity.setText(getResources().getString(R.string.info_contacts_city));
        mCountry.setText(getResources().getString(R.string.info_contacts_country));
        mMobilePhone.setText(getResources().getString(R.string.info_contacts_mobile_phone));
        mHomePhone.setText(getResources().getString(R.string.info_contacts_home_phone));
        mPolitical.setText(getResources().getString(R.string.info_beliefs_political));
        mReligion.setText(getResources().getString(R.string.info_beliefs_religion));
        mLifeMain.setText(getResources().getString(R.string.info_beliefs_life_main));
        mPeopleMain.setText(getResources().getString(R.string.info_beliefs_people_main));
        mSmoking.setText(getResources().getString(R.string.info_beliefs_smoking));
        mAlcohol.setText(getResources().getString(R.string.info_beliefs_alcohol));
        mInspiration.setText(getResources().getString(R.string.info_beliefs_inspiration));
        mActivity.setText(getResources().getString(R.string.info_personal_activity));
        mInterests.setText(getResources().getString(R.string.info_personal_interests));
        mMusic.setText(getResources().getString(R.string.info_personal_music));
        mMovies.setText(getResources().getString(R.string.info_personal_movies));
        mTv.setText(getResources().getString(R.string.info_personal_tv));
        mBooks.setText(getResources().getString(R.string.info_personal_books));
        mGames.setText(getResources().getString(R.string.info_personal_games));
        mQuotes.setText(getResources().getString(R.string.info_personal_quotes));
        mAbout.setText(getResources().getString(R.string.info_about));
    }

    private void onError(Exception e) {
        mProgressBar.setVisibility(View.GONE);

        if (UnknownHostException.class.isInstance(e)) {
            Toast.makeText(getActivity(), "Проверьте подключение и\n" + "      повторите попытку", Toast.LENGTH_SHORT).show();
        } else {
            mErrorText.setVisibility(View.VISIBLE);
            mErrorText.setText(e.getMessage());
        }
    }

    private void setOnlineMode(User user) {
        int onlineMode = user.getOnlineMode();
        if (onlineMode != Api.USER_OFFLINE) {
            mUserState.setText(getResources().getString(Api.USER_ONLINE_STRING));
            if (onlineMode == Api.USER_ONLINE_MOBILE) {
                mOnlineImage.setVisibility(View.VISIBLE);
                mOnlineImage.setImageBitmap(null);
                mOnlineImage.setImageResource(R.drawable.ic_friend_online_mobile);
            } else {
                mOnlineImage.setVisibility(View.VISIBLE);
                mOnlineImage.setImageBitmap(null);
                mOnlineImage.setImageResource(R.drawable.ic_friend_online_computer);
            }
        } else {
            String stringDate = ProcessingUtils.getDate(user.getLastSeen());
            String statePrefix = getResources().getString(Api.USER_STATE);
            if (user.getSex() == Api.USER_SEX_WOMAN) {
                statePrefix += "a";
            }
            mUserState.setText(statePrefix + " " + stringDate);
        }
    }

    private void setStatus(User user) {
        String status = user.getStatus();
        if (status.equals(Api.EMPTY_STRING)) {
            mUserStatus.setVisibility(View.GONE);
        } else {
            mUserStatus.setText(user.getStatus());
        }
    }

    private void setCounters(User user) {
        if (mUserStatus.getVisibility() == View.GONE) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCountersScrollContainer.getLayoutParams();
            params.removeRule(RelativeLayout.BELOW);
            params.addRule(RelativeLayout.BELOW, R.id.user_image);
            mCountersScrollContainer.setLayoutParams(params);
        }

        LinearLayout.LayoutParams counterParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        counterParams.setMargins(16, 16, 16, 0);

        for (Map.Entry<Integer, String> pair : user.getCountersMap().entrySet()) {
            int sizeText = MetricsUtils.spToPx(12, getActivity());
            int sizeCount = MetricsUtils.spToPx(16, getActivity());
            String text = getResources().getString(pair.getKey()) + "\n";
            int border = text.length();
            text += pair.getValue();

            Spannable counter = new SpannableString(text);
            counter.setSpan(new StyleSpan(Typeface.BOLD), border, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            counter.setSpan(new AbsoluteSizeSpan(sizeText), 0, border, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            counter.setSpan(new AbsoluteSizeSpan(sizeCount), border, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            counter.setSpan(new AlignmentSpan() {
                @Override
                public Layout.Alignment getAlignment() {
                    return Layout.Alignment.ALIGN_CENTER;
                }
            }, 0, counter.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            TextView tv = new TextView(getActivity());
            tv.setLayoutParams(counterParams);
            tv.setText(counter);
            mCountersLinearContainer.addView(tv);
        }


    }

    private void setPhotos(Photo[] photos) {
        if (photos.length != 0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(2, 0, 2, 0);

            for (Photo photo : photos) {
                ImageView imageView = new ImageView(getActivity());
                LinearLayout.LayoutParams photoParams = new LinearLayout.LayoutParams(photo.getPhoto200Width(),ViewGroup.LayoutParams.MATCH_PARENT);
                imageView.setLayoutParams(photoParams);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(params);
                mImageLoader.toView(imageView).setCircled(false).byUrl(photo.getPhoto200());
                mPhotosLinearContainer.addView(imageView);
            }
        }
    }

    private void setUserInfo(User user) {
        String birthday;
        String homeTown;
        String relation;
        String langs;
        String skype;
        String site;
        String country;
        String mobilePhone;
        String homePhone;
        String city;
        int political;
        String religion;
        int lifeMain;
        int peopleMain;
        int smoking;
        int alcohol;
        String inspiration;
        String activity;
        String interests;
        String music;
        String movies;
        String tv;
        String books;
        String games;
        String quotes;
        String about;

        //setScrollViewListener
        //general
        if (!(birthday = user.getBirthday()).equals(Api.EMPTY_STRING)) {
            mBirthday.setVisibility(View.VISIBLE);
            mBirthday.append(" " + birthday);
        }
        if (!(homeTown = user.getHomeTown()).equals(Api.EMPTY_STRING)) {
            mHomeTown.setVisibility(View.VISIBLE);
            mHomeTown.append(" " + homeTown);
        }
        if (user.getRelation() != Api.USER_RELATION_UNDEFINED) {
            relation = getResources().getString(user.getRelation());
            mRelation.setVisibility(View.VISIBLE);
            mRelation.append(" " + relation);
        }
        if ((langs = user.getLangs()) != null && !langs.equals(Api.EMPTY_STRING)) {
            mLangs.setVisibility(View.VISIBLE);
            mLangs.append(" " + langs);
        }
        if (mBirthday.getVisibility() == View.GONE &&
                mHomeTown.getVisibility() == View.GONE &&
                mRelation.getVisibility() == View.GONE &&
                mLangs.getVisibility() == View.GONE) {
            mHeadGeneral.setVisibility(View.GONE);
        } else {
            mHeadGeneral.setVisibility(View.VISIBLE);
        }

        //relatives
        User[] relatives = user.getRelatives();
        if (relatives != null) {
            for (User relative : relatives) {
                View layout = View.inflate(getActivity(), R.layout.adapter_friend, null);
                ImageView relativeImage = (ImageView) layout.findViewById(R.id.user_image);
                TextView relativeName = (TextView) layout.findViewById(R.id.user_name);
                TextView relativeType = (TextView) layout.findViewById(R.id.user_status);

                relativeName.setText(relative.getFullName());
                int relationTypeResource = relative.getRelativeType();
                if (relationTypeResource != Api.STRING_RESOURCE_UNDEFINED) {
                    relativeType.setText(getResources().getString(relationTypeResource));
                }
                mImageLoader.toView(relativeImage).setCircled(true).byUrl(relative.getPhoto100());
                mRelativesLinearContainer.addView(layout);
            }
            mHeadRelatives.setVisibility(View.VISIBLE);
            mRelativesLinearContainer.setVisibility(View.VISIBLE);
        } else {
            mRelativesLinearContainer.setVisibility(View.GONE);
        }

        //contact
        if ((city = user.getCity()) != null && !city.equals(Api.EMPTY_STRING)) {
            mCity.setVisibility(View.VISIBLE);
            mCity.append(" " + city);
        }
        if ((country = user.getCountry()) != null && !country.equals(Api.EMPTY_STRING)) {
            mCountry.setVisibility(View.VISIBLE);
            mCountry.append(" " + country);
        }
        if (!(mobilePhone = user.getPhoneMobile()).equals(Api.EMPTY_STRING)) {
            mMobilePhone.setVisibility(View.VISIBLE);
            mMobilePhone.append(" " + mobilePhone);
        }
        if (!(homePhone = user.getPhone()).equals(Api.EMPTY_STRING)) {
            mHomePhone.setVisibility(View.VISIBLE);
            mHomePhone.append(" " + homePhone);
        }
        if (!(skype = user.getSkype()).equals(Api.EMPTY_STRING)) {
            mSkype.setVisibility(View.VISIBLE);
            mSkype.append(" " + skype);
        }
        if (!(site = user.getSite()).equals(Api.EMPTY_STRING)) {
            mSite.setVisibility(View.VISIBLE);
            mSite.append(" " + site);
        }
        if (mCity.getVisibility() == View.GONE &&
                mCountry.getVisibility() == View.GONE &&
                mMobilePhone.getVisibility() == View.GONE &&
                mHomePhone.getVisibility() == View.GONE &&
                mSkype.getVisibility() == View.GONE &&
                mSite.getVisibility() == View.GONE) {
            mHeadContacts.setVisibility(View.GONE);
        } else {
            mHeadContacts.setVisibility(View.VISIBLE);
        }

        //schools
        List<String> schools = user.getSchools();
        if (schools != null) {
            for (String school : schools) {
                LinearLayout layout = new LinearLayout(getActivity());
                TextView schoolInfo = new TextView(getActivity());
                schoolInfo.setText(school);
                layout.addView(schoolInfo);
                mSchoolsLinearContainer.addView(layout);
            }
            mHeadSchools.setVisibility(View.VISIBLE);
            mSchoolsLinearContainer.setVisibility(View.VISIBLE);
        } else {
            mSchoolsLinearContainer.setVisibility(View.GONE);
        }

        //universities
        List<String> universities = user.getUniversities();
        if (universities != null) {
            for (String university : universities) {
                LinearLayout layout = new LinearLayout(getActivity());
                TextView schoolInfo = new TextView(getActivity());
                schoolInfo.setText(university);
                layout.addView(schoolInfo);
                mUniversitiesLinearContainer.addView(layout);
            }
            mHeadUniversities.setVisibility(View.VISIBLE);
            mUniversitiesLinearContainer.setVisibility(View.VISIBLE);
        } else {
            mUniversitiesLinearContainer.setVisibility(View.GONE);
        }

        //beliefs
        if ((political = user.getPolitical()) != Api.STRING_RESOURCE_UNDEFINED) {
            mPolitical.setVisibility(View.VISIBLE);
            mPolitical.append(" " + getResources().getString(political));
        }
        if ((religion = user.getReligion()) != null && !religion.equals(Api.EMPTY_STRING)) {
            mReligion.setVisibility(View.VISIBLE);
            mReligion.append(" " + religion);
        }
        if ((lifeMain = user.getLifeMain()) != Api.STRING_RESOURCE_UNDEFINED) {
            mLifeMain.setVisibility(View.VISIBLE);
            mLifeMain.append(" " + getResources().getString(lifeMain));
        }
        if ((peopleMain = user.getPeopleMain()) != Api.STRING_RESOURCE_UNDEFINED) {
            mPeopleMain.setVisibility(View.VISIBLE);
            mPeopleMain.append(" " + getResources().getString(peopleMain));
        }
        if ((smoking = user.getAttentionToSmoking()) != Api.STRING_RESOURCE_UNDEFINED) {
            mSmoking.setVisibility(View.VISIBLE);
            mSmoking.append(" " + getResources().getString(smoking));
        }
        if ((alcohol = user.getAttentionToAlcohol()) != Api.STRING_RESOURCE_UNDEFINED) {
            mAlcohol.setVisibility(View.VISIBLE);
            mAlcohol.append(" " + getResources().getString(alcohol));
        }
        if ((inspiration = user.getInspiration()) != null && !inspiration.equals(Api.EMPTY_STRING)) {
            mInspiration.setVisibility(View.VISIBLE);
            mInspiration.append(" " + inspiration);
        }
        if (mPolitical.getVisibility() == View.GONE &&
                mReligion.getVisibility() == View.GONE &&
                mLifeMain.getVisibility() == View.GONE &&
                mPeopleMain.getVisibility() == View.GONE &&
                mSmoking.getVisibility() == View.GONE &&
                mAlcohol.getVisibility() == View.GONE &&
                mInspiration.getVisibility() == View.GONE) {
            mHeadBeliefs.setVisibility(View.GONE);
        } else {
            mHeadBeliefs.setVisibility(View.VISIBLE);
        }

        //personal
        if ((activity = user.getActivities()) != null && !activity.equals(Api.EMPTY_STRING)) {
            mActivity.setVisibility(View.VISIBLE);
            mActivity.append(" " + activity);
        }
        if ((interests = user.getInterests()) != null && !interests.equals(Api.EMPTY_STRING)) {
            mInterests.setVisibility(View.VISIBLE);
            mInterests.append(" " + interests);
        }
        if ((music = user.getMusic()) != null && !music.equals(Api.EMPTY_STRING)) {
            mMusic.setVisibility(View.VISIBLE);
            mMusic.append(" " + music);
        }
        if ((movies = user.getMovies()) != null && !movies.equals(Api.EMPTY_STRING)) {
            mMovies.setVisibility(View.VISIBLE);
            mMovies.append(" " + movies);
        }
        if ((tv = user.getTv()) != null && !tv.equals(Api.EMPTY_STRING)) {
            mTv.setVisibility(View.VISIBLE);
            mTv.append(" " + tv);
        }
        if ((books = user.getBooks()) != null && !books.equals(Api.EMPTY_STRING)) {
            mBooks.setVisibility(View.VISIBLE);
            mBooks.append(" " + books);
        }
        if ((games = user.getGames()) != null && !games.equals(Api.EMPTY_STRING)) {
            mGames.setVisibility(View.VISIBLE);
            mGames.append(" " + games);
        }
        if ((quotes = user.getQuotes()) != null && !quotes.equals(Api.EMPTY_STRING)) {
            mQuotes.setVisibility(View.VISIBLE);
            mQuotes.append(" " + quotes);
        }
        if (mActivity.getVisibility() == View.GONE &&
                mInterests.getVisibility() == View.GONE &&
                mMusic.getVisibility() == View.GONE &&
                mMovies.getVisibility() == View.GONE &&
                mBooks.getVisibility() == View.GONE &&
                mGames.getVisibility() == View.GONE &&
                mQuotes.getVisibility() == View.GONE) {
            mHeadPersonal.setVisibility(View.GONE);
        } else {
            mHeadPersonal.setVisibility(View.VISIBLE);
        }

        //about
        if ((about = user.getAbout()) != null && !about.equals(Api.EMPTY_STRING)) {
            mAbout.setVisibility(View.VISIBLE);
            mAbout.append(" " + about);
        }
        if (mAbout.getVisibility() == View.GONE) {
            mHeadAbout.setVisibility(View.GONE);
        } else {
            mHeadAbout.setVisibility(View.VISIBLE);
        }
    }

    //DataLoader
    @Override
    public void onLoadingFinished(User[] users) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mProgressBar.setVisibility(View.GONE);
        if (mScrollContainer.getVisibility() == View.GONE) {
            mScrollContainer.setVisibility(View.VISIBLE);
        }

        User user = users[0];

        mImageLoader.toView(mUserImage).setCircled(true).byUrl(user.getPhoto200());
        mImageLoader.toView(mBackground).setCircled(false).byUrl(user.getPhotoMax());

        mUserName.setText(user.getFullName());

        setOnlineMode(user);
        setStatus(user);
        setCounters(user);
        setUserInfo(user);
    }

    @Override
    public void onLoadingStarted() {
        if (mSwipeRefreshLayout != null && !mSwipeRefreshLayout.isRefreshing()) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadingError(Exception e) {
        mScrollContainer.setVisibility(View.GONE);
        onError(e);
    }
}