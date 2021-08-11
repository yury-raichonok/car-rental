import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Menu, Badge } from 'antd';
import styled from 'styled-components';
import RentalDetailsDataService from '../../services/rentalDetails/RentalDetailsDataService';
import { 
  HomeFilled, 
  CreditCardFilled, 
  BellFilled, 
  ShoppingFilled
} from '@ant-design/icons';
import { useTranslation } from 'react-i18next';
import Marginer from '../marginer/Marginer';

const { SubMenu } = Menu;

const MenuContainer = styled.div`
  .ant-menu-item:hover, .ant-menu-item-active, .ant-menu:not(.ant-menu-inline) .ant-menu-submenu-open, .ant-menu-submenu-active, .ant-menu-submenu-title:hover {
    background-color: #fafafa;
    color: #000;
  }

  .ant-menu-submenu-selected {
    color: #000;
  }

  .ant-menu-submenu:hover > .ant-menu-submenu-title > .ant-menu-submenu-expand-icon, .ant-menu-submenu:hover > .ant-menu-submenu-title > .ant-menu-submenu-arrow {
    color: #000;
  }

  .ant-menu-sub.ant-menu-inline {
    background: #fff;
  }

  .ant-menu-item:hover {
    a {
      text-decoration: none;
      color: #000;
    }
  }

  .ant-menu-item-selected {
    color: #000;
  }

  .ant-menu-item-selected a, .ant-menu-item-selected a:hover {
    color: #000;
  }

  .ant-menu-vertical .ant-menu-item::after, .ant-menu-vertical-left .ant-menu-item::after, .ant-menu-vertical-right .ant-menu-item::after, .ant-menu-inline .ant-menu-item::after {
    border-right: 3px solid #f44336;
  }

  .ant-menu:not(.ant-menu-horizontal) .ant-menu-item-selected {
    background-color: #fafafa;
  }
`;

const Row = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: center;
`;

const ProfileMenuComponent = () => {

  const { t } = useTranslation();

  const [data, setData] = useState({});

  const fetchData = async () => {

    const resp = await RentalDetailsDataService.getUserDetailsStatistic().catch((err) => {
      console.log("Error: ", err);
    });

    if(resp) {
      setData(resp.data);
      console.log(resp);
    }
  }

  useEffect(() => {
    fetchData();
  }, []);
  
  return (
    <MenuContainer>
      <Menu mode="inline" style={{ width: 256 }}>
        <Menu.Item key="1" icon={<HomeFilled />}>
          <Link to="/profile"><div>{t('profile')}</div></Link>
        </Menu.Item>
        <SubMenu key="sub1" icon={<CreditCardFilled />} title={
            <Row>
              {t('bills')}
              <Marginer direction="horizontal" margin={10} />
              <Badge count={data.paymentBills}/>
              <Badge style={{background: "rgb(40 156 255 / 70%)"}} count={data.repairBills}/>
            </Row>
          }>
          <Menu.Item key="2">
            <Link to="/profile/paymentbills"><div>{t('payment_bills')}</div></Link>
            <Marginer direction="horizontal" margin={10} />
            <Badge count={data.paymentBills}/>
          </Menu.Item>
          <Menu.Item key="3">
            <Link to="/profile/repairbills"><div>{t('repair_bills')}</div></Link>
            <Marginer direction="horizontal" margin={10} />
            <Badge count={data.repairBills} style={{background: "rgb(40 156 255 / 70%)"}}/>
          </Menu.Item>
          <Menu.Item key="4">
            <Link to="/profile/paymentbills/history"><div>{t('payment_bills_history')}</div></Link>
          </Menu.Item>
          <Menu.Item key="5">
            <Link to="/profile/repairbills/history"><div>{t('repair_bills_history')}</div></Link>
          </Menu.Item>
        </SubMenu>
        <SubMenu key="sub2" icon={<ShoppingFilled />} title={
            <Row>
              {t('orders')}
              <Marginer direction="horizontal" margin={10} />
              <Badge count={data.orders}/>
            </Row>
          }>
          <Menu.Item key="6">
            <Link to="/profile/orders"><div>{t('orders')}</div></Link>
            <Marginer direction="horizontal" margin={10} />
            <Badge count={data.orders}/>
          </Menu.Item>
          <Menu.Item key="7">
            <Link to="/profile/orders/history"><div>{t('orders_history')}</div></Link>
          </Menu.Item>
        </SubMenu>
        <SubMenu key="sub3" icon={<BellFilled />} title={
            <Row>
              {t('notifications')}
              <Marginer direction="horizontal" margin={10} />
              <Badge count={data.notifications}/>
            </Row>
          }>
          <Menu.Item key="8">
            <Link to="/profile/notifications"><div>{t('notifications')}</div></Link>
            <Marginer direction="horizontal" margin={10} />
            <Badge count={data.notifications}/>
          </Menu.Item>
          <Menu.Item key="9">
            <Link to="/profile/notifications/history"><div>{t('notifications_history')}</div></Link>
          </Menu.Item>
        </SubMenu>
      </Menu>      
    </MenuContainer>
  )
}

export default ProfileMenuComponent
