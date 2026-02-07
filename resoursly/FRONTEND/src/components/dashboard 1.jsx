import React, { useEffect, useState } from 'react'
import SMEList from './SMEList';
import searchClient from '../AxiosClient/SearchClient';
import axios from "axios";
const Dashboard = () => {
    const [userEmail, setUserEmail] = useState('User');
    const [profilesCount, setProfilesCount] = useState(0);
    const [projects, setProjects] = useState([]);
    const [selectedProject, setSelectedProject] = useState(null);

    useEffect(() => {
        const mail = localStorage.getItem("userDisplayName");
        if(mail) {
            setUserEmail(mail);
        }  

     
    }, [userEmail]);

    useEffect(() => {
     
        axios
            .get("http://localhost:8040/projects/pronameid")
            .then((response) => {
            setProjects(response.data);
            })
            .catch((error) => {
            console.error("Error fetching projects:", error);
            });
    }, []); 

    const fetchApprovedProfilesCount = async () => {
        const res = await searchClient
            .get(`/total`)
            .then((response) => response)
            .catch((error) => {
                console.log(
                    'There is some error while fetching approved profiles count',
                    error
                );
            });

        
        setProfilesCount(res.data.approvedCount);
    };
    



    
    useEffect(() => {
        fetchApprovedProfilesCount();
    }, []);
    
    return (<>
    <h1>Hi {userEmail}!, welcome back to the platfrom</h1><br/>

    <h3>Total Resources : {profilesCount}</h3>

   
 

    
    <br></br>
    <h3>List of Projects</h3>
     <table><tr className="px-4 py-3"> <th width="150PX" align='LEFT'>PRODJECT ID</th><th  width="200PX" align='LEFT'>NAME</th></tr> {projects.map((project) => (
        
        <tr className="px-4 py-3"> <td>{project.projectId} </td><td> {project.projectName}</td>
            
            
          
        </tr>
        ))} </table> 
  
    
        <SMEList/>
</>);};
export default Dashboard;
