import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Menu, Badge } from 'antd';
import styled from 'styled-components';
import MessageDataService from '../../services/message/MessageDataService';
import RequestDataService from '../../services/request/RequestDataService';
import OrderDataService from '../../services/order/OrderDataService';
import { 
  CarFilled, 
  HomeFilled, 
  CreditCardFilled, 
  EditFilled,
  MailFilled, 
  IdcardFilled, 
  QuestionCircleFilled, 
  EnvironmentFilled, 
  ReadFilled 
} from '@ant-design/icons';
import Marginer from '../marginer/Marginer';
import { useTranslation } from 'react-i18next';

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

const AdminMenuComponent = (props) => {

  const { t } = useTranslation();

  const [messages, setMessages] = useState();
  const [orders, setOrders] = useState();
  const [requests, setRequests] = useState();

  const fetchData = async () => {

    const messagesResp = await MessageDataService.findNewMessagesAmount();
    const ordersResp = await OrderDataService.findNewOrdersAmount();
    const requestsResp = await RequestDataService.findNewRequestsAmount();

    if(messagesResp && ordersResp && requestsResp) {
      setMessages(messagesResp.data);
      setOrders(ordersResp.data);
      setRequests(requestsResp.data);
    }
  }

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <MenuContainer>
      <Menu mode="inline">
        <Menu.Item key="1" icon={<HomeFilled />}>
          <Link to="/admin"><div>{t('dashboard')}</div></Link>
        </Menu.Item>
        <SubMenu key="sub1" icon={<CreditCardFilled />} title={t('bills')}>
          <Menu.Item key="2">
            <Link to="/admin/paymentbills"><div>{t('payment_bills')}</div></Link>
          </Menu.Item>
          <Menu.Item key="3">
            <Link to="/admin/repairbills"><div>{t('repair_bills')}</div></Link>
          </Menu.Item>
        </SubMenu>
        <SubMenu key="sub2" icon={<CarFilled />} title={t('cars')}>
          <Menu.Item key="4">
            <Link to="/admin/cars"><div>{t('cars')}</div></Link>
          </Menu.Item>
          <Menu.Item key="5">
            <Link to="/admin/brands"><div>{t('brands')}</div></Link>
          </Menu.Item>
          <Menu.Item key="6">
            <Link to="/admin/models"><div>{t('models')}</div></Link>
          </Menu.Item>
          <Menu.Item key="7">
            <Link to="/admin/classes"><div>{t('classes')}</div></Link>
          </Menu.Item>
        </SubMenu>
        <Menu.Item key="8" icon={<QuestionCircleFilled />}>
          <Link to="/admin/faqs"><div>{t('faqs')}</div></Link>
        </Menu.Item>
        <Menu.Item key="9" icon={<EnvironmentFilled />}>
          <Link to="/admin/locations"><div>{t('rental_locations')}</div></Link>
        </Menu.Item>
        <SubMenu key="sub3" icon={<MailFilled />} title={
          <Row>
            {t('messages')}
            <Marginer direction="horizontal" margin={10} />
            <Badge count={messages}/>
          </Row>
          }>
          <Menu.Item key="10">
            <Link to="/admin/messages/"><div>{t('all_messages')}</div></Link>
          </Menu.Item>
          <Menu.Item key="11">
            <Link to="/admin/messages/new"><div>{t('New_messages')}</div></Link>
            <Marginer direction="horizontal" margin={10} />
            <Badge count={messages}/>
          </Menu.Item>
        </SubMenu>
        <SubMenu key="sub4" icon={<EditFilled />} title={
          <Row>
            {t('requests')}
            <Marginer direction="horizontal" margin={10} />
            <Badge count={requests} />
          </Row>
          }>
          <Menu.Item key="12">
            <Link to="/admin/requests/"><div>{t('all_requests')}</div></Link>
          </Menu.Item>
          <Menu.Item key="13">
            <Link to="/admin/requests/new"><div>{t('New_requests')}</div></Link>
            <Marginer direction="horizontal" margin={10} />
            <Badge count={requests} />
          </Menu.Item>
        </SubMenu>
        <SubMenu key="sub5" icon={<ReadFilled />} title={
          <Row>
            {t('orders')}
            <Marginer direction="horizontal" margin={10} />
            <Badge count={orders} />
          </Row>
        }>
          <Menu.Item key="14">
            <Link to="/admin/orders/"><div>{t('all_orders')}</div></Link>
          </Menu.Item>
          <Menu.Item key="15">
            <Link to="/admin/orders/new"><div>{t('New_orders')}</div></Link>
            <Marginer direction="horizontal" margin={10} />
            <Badge count={orders} />
          </Menu.Item>
          <Menu.Item key="16">
            <Link to="/admin/orders/current"><div>{t('current_orders')}</div></Link>
          </Menu.Item>
          <Menu.Item key="17">
            <Link to="/admin/orders/future"><div>{t('future_orders')}</div></Link>
          </Menu.Item>
        </SubMenu>
        <Menu.Item key="18" icon={<IdcardFilled />}>
          <Link to="/admin/users"><div>{t('users')}</div></Link>
        </Menu.Item>
      </Menu>      
    </MenuContainer>
  )
}

export default AdminMenuComponent
