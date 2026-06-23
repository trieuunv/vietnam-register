import { useRoutes } from "react-router-dom";
import MainLayout from "../components/MainLayout";
import { path } from "./path";
import Home from "../pages/Home";
import Login from "../pages/Auth/Login/Loadable";
import Profile from "../pages/User/Profile";
import Test from "../pages/Test";
import StaffLayout from "@/components/StaffLayout";
import Dashboard from "@/pages/Dashboard";
import Inspection from "@/pages/Inspection/Loadable";
import Vehicle from "@/pages/Vehicle/Loadable";
import Appointment from "@/pages/Appointment/Loadable";
import InspectionDetail from "@/pages/InspectionDetail/Loadable";
import AppointmentDetail from "@/pages/AppointmentDetail/Loadable";
import VehicleDetail from "@/pages/VehicleDetail/Loadable";
import OwnerDetail from "@/pages/OwnerDetail/Loadable";

const Routes = () => {
    const routes = useRoutes([
        {
            element: <MainLayout />,
            children: [
                { path: path.home, element: <Home /> },
                { path: path.profile, element: <Profile /> }
            ],
        },
        {
            element: <StaffLayout />,
            children: [
                { path: path.dashboard, element: <Dashboard /> },
                { path: path.inspection, element: <Inspection /> },
                { path: path.vehicle, element: <Vehicle /> },
                { path: path.vehicleDetail, element: <VehicleDetail /> },
                { path: path.appointment, element: <Appointment /> },
                { path: path.appointmentDetail, element: <AppointmentDetail /> },
                { path: path.inspectionDetail, element: <InspectionDetail /> },
                { path: path.ownerDetail, element: <OwnerDetail /> },
            ]
        },
        {
            path: path.login,
            element: <Login />
        },
        {
            path: path.test,
            element: <Test />
        }
    ]);

    return routes;
};

export default Routes;