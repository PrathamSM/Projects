import React, { useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';
import { showProfile, viewOnlyProfile } from '../feature/PopupSlice';
import { setProfileId } from '../feature/CurrentProfile';
import { editProfileOn } from '../feature/EditMode';
import searchClient from '../AxiosClient/SearchClient';
import ReusableTable from '../components/ReusableTable';
import { data } from 'react-router-dom';

function List({columns, dataToLoad}) {
  const dispatch = useDispatch();
  console.log(dataToLoad);

  const viewClickHandler = (profileId) => {
    dispatch(setProfileId(profileId));
    dispatch(showProfile());
    dispatch(viewOnlyProfile());
  };

  const editClickHandler = (profileId) => {
    dispatch(setProfileId(profileId));
    dispatch(showProfile());
    dispatch(editProfileOn());
  };


  return (
    <ReusableTable 
      columns={columns} 
      data={dataToLoad} 
      onViewClick={viewClickHandler} 
      onEditClick={editClickHandler} 
      searchKeys={['name', 'email', 'designation', 'primary_skills', 'location']}
    />
  );
}

export default List;
