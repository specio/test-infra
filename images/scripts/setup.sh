git clone https://github.com/BRMcLaren/openenclave.git

# Install Ansible
n=0
until [ "$n" -ge 5 ]
do
   openenclave/scripts/ansible/install-ansible.sh && break
   n=$((n+1)) 
   sleep 15
done

# Run Ansible Playbook
n=0
until [ "$n" -ge 5 ]
do
   ansible-playbook openenclave/scripts/ansible/oe-contributors-acc-setup.yml && break
   n=$((n+1)) 
   sleep 15
done

# Remove openenclave
rm -rf openenclave